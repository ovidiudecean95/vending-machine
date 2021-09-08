package com.mvpmatch.vendingmachine.validator.product;

import com.mvpmatch.vendingmachine.model.Product;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.service.UserAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Slf4j
public class ProductEntityIdValidator implements ConstraintValidator<ProductEntityId, Integer> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserAuth userAuth;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return userAuth.getUser().getId().equals(product.get().getSeller().getId());
        }
        return false;
    }

}
