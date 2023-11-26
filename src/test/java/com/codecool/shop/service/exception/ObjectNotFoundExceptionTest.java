package com.codecool.shop.service.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectNotFoundExceptionTest {
    @Test
    void testEmailNotFoundExceptionMessage() {
        // given
        UUID id = UUID.randomUUID();
        Class<Void> aClass = Void.class;

        // then
        try {
            throw new ObjectNotFoundException(id, aClass);
        } catch (ObjectNotFoundException ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("Object of a class " + aClass.getSimpleName() +
                            " and id " + id + " does not exist");
        }
    }
}