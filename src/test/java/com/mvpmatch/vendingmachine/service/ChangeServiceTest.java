package com.mvpmatch.vendingmachine.service;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.view.CoinView;
import com.mvpmatch.vendingmachine.model.CoinInventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChangeServiceTest extends VendingMachineAbstractTest {

    @Autowired
    private ChangeService changeService;

    @Test
    public void testGetAmount_ReturnOneCoin() {
        List<CoinView> coinViews = changeService.getAmountInCoins(100, List.of(
                CoinInventory.builder().coinValue(100).amount(1).build()
        ));

        assertEquals(1, coinViews.size());
        assertEquals(1, coinViews.get(0).getAmount());
        assertEquals(100, coinViews.get(0).getValue());
    }

    @Test
    public void testGetAmount_ReturnTwoCoins() {
        List<CoinView> coinViews = changeService.getAmountInCoins(100, List.of(
                CoinInventory.builder().coinValue(50).amount(4).build()
        ));

        assertEquals(1, coinViews.size());
        assertEquals(2, coinViews.get(0).getAmount());
        assertEquals(50, coinViews.get(0).getValue());
    }

    @Test
    public void testGetAmount_ReturnMultipleCoins() {
        List<CoinView> coinViews = changeService.getAmountInCoins(30, List.of(
                CoinInventory.builder().coinValue(10).amount(2).build(),
                CoinInventory.builder().coinValue(5).amount(10).build()
        ));

        assertEquals(2, coinViews.size());
        assertEquals(2, coinViews.get(0).getAmount());
        assertEquals(10, coinViews.get(0).getValue());
        assertEquals(2, coinViews.get(1).getAmount());
        assertEquals(5, coinViews.get(1).getValue());
    }

    @Test()
    public void testGetAmount_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> {
            changeService.getAmountInCoins(50, List.of(
                    CoinInventory.builder().coinValue(10).amount(2).build()
            ));
        });
    }

}
