package com.codecool.shop.service.validator;

import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.EmailNotFoundException;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerValidatorTest {
    @InjectMocks
    CustomerValidator validator;
    @Mock
    CustomerRepository repository;

    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
    }

    @Test
    void testValidateEntityById_ShouldReturnObject_WhenExist() {
        // when
        Mockito.when(repository.findById(customerId)).thenReturn(Optional.of(new Customer()));

        // then
        assertThatCode(() -> validator.validateByEntityId(customerId))
                .doesNotThrowAnyException();
    }

    @Test
    void testValidateEntityById_ShouldThrownObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(repository.findById(customerId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEntityId(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testValidateByEmail_ShouldReturnObject_WhenExist() {
        // when
        Mockito.when(repository.findByEmail("Email")).thenReturn(Optional.of(new Customer()));

        // then
        assertThatCode(() -> validator.validateByEmail("Email"))
                .doesNotThrowAnyException();
    }

    @Test
    void testValidateByEmail_ShouldThrownObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(repository.findByEmail("Email")).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEmail("Email"))
                .isInstanceOf(EmailNotFoundException.class);
    }

}