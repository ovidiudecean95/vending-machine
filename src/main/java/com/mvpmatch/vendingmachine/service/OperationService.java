package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.BuyProductsRequest;
import com.mvpmatch.vendingmachine.dto.DepositCoinRequest;
import com.mvpmatch.vendingmachine.dto.view.*;
import com.mvpmatch.vendingmachine.mapper.ProductMapper;
import com.mvpmatch.vendingmachine.mapper.UserMapper;
import com.mvpmatch.vendingmachine.model.CoinInventory;
import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.CoinInventoryRepository;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
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
import java.util.stream.Collectors;

@Service
public class OperationService {

    @Autowired
    private UserAuth userAuth;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CoinInventoryRepository coinInventoryRepository;

    @Autowired
    private ChangeService changeService;

    @Transactional
    public CoinsView resetDeposit() {
        User user = userAuth.getUser();
        List<CoinInventory> coinsInventory = coinInventoryRepository.findAll();
        List<CoinView> returnedCoins = changeService.getAmountInCoins(user.getDeposit(), coinsInventory);
        updateCoinInventoryAfterWithdraw(returnedCoins, coinsInventory);

        user.setDeposit(0);
        userRepository.save(user);

        return new CoinsView(returnedCoins);
    }

    public void updateCoinInventoryAfterWithdraw(List<CoinView> withdrawCoins, List<CoinInventory> coinsInventory) {
        coinsInventory.forEach(
                coin -> {
                    Optional<CoinView> withdrawCoin = withdrawCoins.stream()
                            .filter(coinView -> coinView.getValue().equals(coin.getCoinValue())).findFirst();
                    if (withdrawCoin.isPresent()) {
                        coin.setAmount(coin.getAmount() - withdrawCoin.get().getAmount());
                        coinInventoryRepository.save(coin);
                    }
                }
        );
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

    @Transactional
    public BuyView buyProducts(BuyProductsRequest buyProductsRequest) {
        User currentUser = userAuth.getUser();
        Product product = productRepository.getById(buyProductsRequest.getProductId());
        Integer spent = product.getCost() * buyProductsRequest.getAmount();

        List<CoinView> changeCoins = changeService.getAmountInCoins(
                currentUser.getDeposit() - spent, coinInventoryRepository.findAll());
        updateCoinInventoryAfterWithdraw(changeCoins, coinInventoryRepository.findAll());

        currentUser.setDeposit(0);
        userRepository.save(currentUser);

        product.setAmountAvailable(product.getAmountAvailable() - buyProductsRequest.getAmount());
        productRepository.save(product);

        return new BuyView(spent, changeCoins, productMapper.from(product, buyProductsRequest));
    }
}
