package com.codecool.shop.service.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailNotFoundExceptionTest {
    @Test
    void testEmailNotFoundExceptionMessage() {
        // given
        String email = "Email@wp.pl";

        // then
        try {
            throw new EmailNotFoundException(email);
        } catch (EmailNotFoundException ex) {
            assertThat(ex.getMessage()).isEqualTo("Email " + email + " cannot be found");
        }
    }
}