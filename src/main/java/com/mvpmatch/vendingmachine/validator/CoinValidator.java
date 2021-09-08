package com.mvpmatch.vendingmachine.validator;

import com.mvpmatch.vendingmachine.model.CoinInventory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CoinValidator implements ConstraintValidator<Coin, Integer> {

    @Override
    public boolean isValid(Integer coin, ConstraintValidatorContext constraintValidatorContext) {
        return coin != null && CoinInventory.VALID_COINS.contains(coin);
    }
}
