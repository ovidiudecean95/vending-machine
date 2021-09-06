package com.mvpmatch.vendingmachine.validator;

import com.mvpmatch.vendingmachine.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<Role, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        return User.ROLES.contains(role);
    }

}
