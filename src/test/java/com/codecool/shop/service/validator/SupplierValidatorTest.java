package com.codecool.shop.service.validator;

import com.codecool.shop.repository.SupplierRepository;
import com.codecool.shop.repository.entity.Supplier;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class SupplierValidatorTest {
    @InjectMocks
    SupplierValidator validator;
    @Mock
    SupplierRepository repository;

    private UUID supplierId;

    @BeforeEach
    void setUp() {
        supplierId = UUID.randomUUID();
    }

    @Test
    void testValidateEntityById_ShouldReturnObject_WhenExist() {
        // when
        Mockito.when(repository.findById(supplierId)).thenReturn(Optional.of(new Supplier()));
        Supplier supplier = validator.validateByEntityId(supplierId);

        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void testValidateEntityById_ShouldThrownObjectNotFoundException_WhenNoSupplier() {
        // when
        Mockito.when(repository.findById(supplierId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEntityId(supplierId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}