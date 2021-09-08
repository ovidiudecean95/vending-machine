package com.mvpmatch.vendingmachine.validator.buy;

import com.mvpmatch.vendingmachine.dto.BuyProductsRequest;
import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.model.User;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.service.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class BuyProductsValidator implements ConstraintValidator<BuyProducts, BuyProductsRequest> {

    @Autowired
    private UserAuth userAuth;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean isValid(BuyProductsRequest buyProductsRequest, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        User currentUser = userAuth.getUser();
        Optional<Product> optionalProduct = productRepository.findById(buyProductsRequest.getProductId());

        if (optionalProduct.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Product doesn't exist").addConstraintViolation();
            return false;
        }

        Product product = optionalProduct.get();
        if (currentUser.getDeposit() < product.getCost() * buyProductsRequest.getAmount()) {
            context.buildConstraintViolationWithTemplate("Insufficient funds").addConstraintViolation();
            return false;
        }

        if (product.getAmountAvailable() < buyProductsRequest.getAmount()) {
            context.buildConstraintViolationWithTemplate("Insufficient amount").addConstraintViolation();
            return false;
        }

        return true;
    }
}
