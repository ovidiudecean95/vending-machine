package com.mvpmatch.vendingmachine.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz, Integer id) {
        super(String.format("%s with id '%d' was not found", clazz.getSimpleName(), id));
    }

}
