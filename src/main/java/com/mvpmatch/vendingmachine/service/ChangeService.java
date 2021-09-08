package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.dto.view.CoinView;
import com.mvpmatch.vendingmachine.model.CoinInventory;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ChangeService {

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

}
