package com.mvpmatch.vendingmachine.validator;

import com.mvpmatch.vendingmachine.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

public class RoleValidator implements ConstraintValidator<Role, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        List<String> prefixedRoles = User.ROLES.stream().map(existingRole -> "ROLE_" + existingRole).collect(Collectors.toList());
        return role != null && prefixedRoles.contains(role);
    }

}
