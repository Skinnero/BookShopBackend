package com.codecool.shop.repository.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(
                UUID.randomUUID(),
                "Name",
                "Description",
                BigDecimal.valueOf(11),
                "PLN"
        );
    }

    @Test
    void testEquals_ShouldReturnTrue_WhenObjectsHaveSameValues() {
        // given
        Product otherProduct = new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCurrency()
        );

        // then
        assertThat(product).isEqualTo(otherProduct);
        assertThat(product.hashCode()).isEqualTo(otherProduct.hashCode());
    }

    @Test
    void testEquals_ShouldReturnFalse_WhenObjectsHaveDifferentValues() {
        // given
        Product otherProduct = new Product(
                UUID.randomUUID(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCurrency()
        );

        // then
        assertThat(product).isNotEqualTo(otherProduct);
        assertThat(product.hashCode()).isNotEqualTo(otherProduct.hashCode());
    }
}
