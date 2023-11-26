package com.codecool.shop.controller.requestvalidator;

import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UnoccupiedEmailValidatorTest {
    @InjectMocks
    UnoccupiedEmailValidator validator;
    @Mock
    CustomerRepository repository;

    @Test
    void testIsValid_ShouldReturnTrue_WhenNoCustomerWithSuchEmail() {
        // when
        Mockito.when(repository.findByEmail("Email")).thenReturn(Optional.empty());

        // then
        assertThat(validator.isValid("Email", null)).isTrue();
    }

    @Test
    void testIsValid_ShouldReturnFalse_WhenCustomerWithSuchEmailExist() {
        // when
        Mockito.when(repository.findByEmail("Email")).thenReturn(Optional.of(new Customer()));

        // then
        assertThat(validator.isValid("Email", null)).isFalse();
    }
}