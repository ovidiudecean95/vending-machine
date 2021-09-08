package com.mvpmatch.vendingmachine.validator.product;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductPriceValidator implements ConstraintValidator<ProductPrice, Integer> {

    @Override
    public boolean isValid(Integer price, ConstraintValidatorContext constraintValidatorContext) {
        return  price != null && price > 0 && price % 5 == 0;
    }
}
