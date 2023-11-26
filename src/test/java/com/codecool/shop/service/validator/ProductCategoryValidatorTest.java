package com.codecool.shop.service.validator;

import com.codecool.shop.repository.ProductCategoryRepository;
import com.codecool.shop.repository.entity.ProductCategory;
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
class ProductCategoryValidatorTest {
    @InjectMocks
    ProductCategoryValidator validator;
    @Mock
    ProductCategoryRepository repository;

    private UUID productCategoryId;

    @BeforeEach
    void setUp() {
        productCategoryId = UUID.randomUUID();
    }

    @Test
    void testValidateEntityById_ShouldReturnObject_WhenExist() {
        // when
        Mockito.when(repository.findById(productCategoryId)).thenReturn(Optional.of(new ProductCategory()));
        ProductCategory productCategory = validator.validateByEntityId(productCategoryId);

        // then
        assertThat(productCategory).isNotNull();
    }

    @Test
    void testValidateEntityById_ShouldThrownObjectNotFoundException_WhenNoProductCategory() {
        // when
        Mockito.when(repository.findById(productCategoryId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEntityId(productCategoryId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

}