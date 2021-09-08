package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.DepositCoinRequest;
import com.mvpmatch.vendingmachine.dto.view.CoinView;
import com.mvpmatch.vendingmachine.dto.view.CoinsView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.mapper.UserMapper;
import com.mvpmatch.vendingmachine.model.CoinInventory;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.CoinInventoryRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class OperationService {

    @Autowired
    private UserAuth userAuth;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoinInventoryRepository coinInventoryRepository;

    @Transactional
    public CoinsView resetDeposit() {
        User user = userAuth.getUser();
        List<CoinInventory> coinsInventory = coinInventoryRepository.findAll();
        List<CoinView> returnedCoins = getAmountInCoins(user.getDeposit(), coinsInventory);

        coinsInventory.forEach(
            coin -> {
                Optional<CoinView> withdrawCoin = returnedCoins.stream()
                        .filter(coinView -> coinView.getValue().equals(coin.getCoinValue())).findFirst();
                if (withdrawCoin.isPresent()) {
                    coin.setAmount(coin.getAmount() - withdrawCoin.get().getAmount());
                    coinInventoryRepository.save(coin);
                }
            }
        );

        user.setDeposit(0);
        userRepository.save(user);

        return new CoinsView(returnedCoins);
    }

    public List<CoinView> getAmountInCoins(Integer amount, List<CoinInventory> coinInventory) {
        AtomicInteger amountAtomic = new AtomicInteger(amount);
        Map<Integer, Integer> coinInventoryByValue = coinInventory.stream()
                .collect(Collectors.toMap(CoinInventory::getCoinValue, CoinInventory::getAmount));
        List<CoinView> coinsResult = new ArrayList<>();

        CoinInventory.VALID_COINS.stream().sorted((c1, c2) -> c2 - c1).forEach(
            coinValue -> {
                CoinView coinView = CoinView.builder().value(coinValue).amount(0).build();

                while (amountAtomic.get() >= coinValue &&
                        coinInventoryByValue.containsKey(coinValue) && coinInventoryByValue.get(coinValue) > 0) {
                    amountAtomic.addAndGet(-coinValue);
                    coinInventoryByValue.put(coinValue, coinInventoryByValue.get(coinValue) - 1);
                    coinView.incrementAmount();
                }

                if (coinView.getAmount() > 0) {
                    coinsResult.add(coinView);
                }
            }
        );

        if (amountAtomic.get() != 0) {
            throw new ValidationException("Insufficient coins");
        }

        return coinsResult;
    }


    @Transactional
    public UserView deposit(DepositCoinRequest depositCoinRequest) {
        User user = userAuth.getUser();
        user.setDeposit(user.getDeposit() + depositCoinRequest.getCoin());
        userRepository.save(user);

        CoinInventory coinInventory = coinInventoryRepository.findByCoinValue(depositCoinRequest.getCoin());
        if (coinInventory == null) {
            coinInventory = CoinInventory.builder().coinValue(depositCoinRequest.getCoin()).amount(0).build();
            coinInventoryRepository.save(coinInventory);
        }

        coinInventory.incrementAmount();
        coinInventoryRepository.save(coinInventory);

        return userMapper.userToUserView(user);
    }
}
