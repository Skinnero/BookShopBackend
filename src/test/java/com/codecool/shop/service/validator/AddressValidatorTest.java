package com.codecool.shop.service.validator;

import com.codecool.shop.repository.AddressRepository;
import com.codecool.shop.repository.entity.Address;
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
public class AddressValidatorTest {
    @InjectMocks
    AddressValidator validator;
    @Mock
    AddressRepository repository;

    private UUID addressId;

    @BeforeEach
    void setUp() {
        addressId = UUID.randomUUID();
    }

    @Test
    void testValidateEntityById_ShouldNotThrowAndError_WhenExist() {
        // when
        Mockito.when(repository.findById(addressId)).thenReturn(Optional.of(new Address()));

        // then
        assertThatCode(() -> validator.validateByEntityId(addressId))
                .doesNotThrowAnyException();
    }

    @Test
    void testValidateEntityById_ShouldThrownObjectNotFoundException_WhenNoAddress() {
        // when
        Mockito.when(repository.findById(addressId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEntityId(addressId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}