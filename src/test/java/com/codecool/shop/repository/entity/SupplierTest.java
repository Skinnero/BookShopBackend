package com.codecool.shop.repository.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SupplierTest {
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
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
        supplier.assignAllProductsToSupplier(productList);

        // then
        assertThat(supplier.getProducts().size()).isEqualTo(2);
    }
}
