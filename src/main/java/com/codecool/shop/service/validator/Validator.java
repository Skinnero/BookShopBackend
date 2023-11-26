package com.codecool.shop.service.validator;

import java.util.UUID;

public interface Validator<T> {
    T validateByEntityId(UUID id);
}
