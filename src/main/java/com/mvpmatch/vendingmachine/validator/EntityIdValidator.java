package com.mvpmatch.vendingmachine.validator;

import com.mvpmatch.vendingmachine.model.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EntityIdValidator implements ConstraintValidator<EntityId, Integer> {

    @Autowired
    private EntityManager entityManager;

    private Class<? extends BaseEntity> clazz;

    @Override
    public void initialize(EntityId constraintAnnotation) {
        clazz = constraintAnnotation.clazz();
    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return entityManager.find(clazz, id) != null;
    }
}
