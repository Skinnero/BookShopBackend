package com.codecool.shop.service.exception;

import com.codecool.shop.repository.entity.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectAlreadyExistExceptionExceptionTest {

    @Test
    void testObjectAlreadyExistExceptionMessage() {
        // given
        Class<?> aClass = Product.class;

        // then
        try {
            throw new ObjectAlreadyExistException(aClass);
        } catch (ObjectAlreadyExistException ex) {
            assertThat(ex.getMessage()).isEqualTo("Object of a class %s already exist", aClass.getSimpleName());
        }
    }
}
