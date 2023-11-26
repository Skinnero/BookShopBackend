package com.codecool.shop.service.validator;

import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
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
public class ProductValidatorTest {
    @InjectMocks
    ProductValidator validator;
    @Mock
    ProductRepository repository;

    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
    }

    @Test
    void testValidateEntityById_ShouldReturnObject_WhenExist() {
        // when
        Mockito.when(repository.findById(productId)).thenReturn(Optional.of(new Product()));
        Product product = validator.validateByEntityId(productId);

        // then
        assertThat(product).isNotNull();
    }

    @Test
    void testValidateEntityById_ShouldThrownObjectNotFoundException_WhenNoProduct() {
        // when
        Mockito.when(repository.findById(productId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEntityId(productId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}