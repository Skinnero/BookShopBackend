package com.codecool.shop.service.exception;

import java.util.UUID;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(UUID id, Class<?> aClass) {
        super(String.format("Object of a class %s and id %s does not exist", aClass.getSimpleName(), id));
    }
    public ObjectNotFoundException(Class<?> aClass) {
        super(String.format("Object of a class %s does not exist", aClass.getSimpleName()));
    }

}
