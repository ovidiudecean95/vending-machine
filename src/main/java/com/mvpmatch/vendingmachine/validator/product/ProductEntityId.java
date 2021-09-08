package com.mvpmatch.vendingmachine.validator.product;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductEntityIdValidator.class)
@Documented
public @interface ProductEntityId {

    String message() default "Product doesn't exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
