package com.codecool.shop.service.exception;

public class ObjectAlreadyExistException extends RuntimeException {
    public ObjectAlreadyExistException(Class<?> aClass) {
        super(String.format("Object of a class %s already exist", aClass.getSimpleName()));
    }
}
