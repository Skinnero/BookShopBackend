package com.codecool.shop.repository.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductCategoryTest {
    private ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        productCategory = new ProductCategory();
    }

    @Test
    void testAssignAllProductsToSupplier_ShouldAddOnlyThoseProductsWhichAreNotThereYes_WhenCalled() {
        // given
        Product productOne = new Product();
        productOne.setId(UUID.randomUUID());

        Product productTwo = new Product();
        productTwo.setId(UUID.randomUUID());
        List<Product> productList = List.of(productOne, productOne, productTwo, productTwo);

        // when
        productCategory.assignAllProductsToProductCategory(productList);

        // then
        assertThat(productCategory.getProducts().size()).isEqualTo(2);
    }
}
