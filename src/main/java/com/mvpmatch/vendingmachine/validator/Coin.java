package com.mvpmatch.vendingmachine.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CoinValidator.class)
@Documented
public @interface Coin {

    String message() default "Invalid coin";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
