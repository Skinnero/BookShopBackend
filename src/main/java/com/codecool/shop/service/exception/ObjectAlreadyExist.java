package com.codecool.shop.service.exception;

public class ObjectAlreadyExist extends RuntimeException {
    public ObjectAlreadyExist(Class<?> aClass) {
        super(String.format("Object of a class %s already exist", aClass.getSimpleName()));
    }
}
