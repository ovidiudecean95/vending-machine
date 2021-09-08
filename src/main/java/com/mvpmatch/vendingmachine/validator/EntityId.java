package com.mvpmatch.vendingmachine.validator;

import com.mvpmatch.vendingmachine.model.BaseEntity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntityIdValidator.class)
@Documented
public @interface EntityId {

    String message() default "Entity doesn't exist";

    Class<? extends BaseEntity> clazz();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
