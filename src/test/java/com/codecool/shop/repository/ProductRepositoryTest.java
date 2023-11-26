package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    ProductRepository repository;

    private UUID productId;
    private UUID supplierId;
    private UUID productCategoryId;
    @BeforeEach
    void setUp() {
        productId = UUID.fromString("5b338991-267d-4cff-b121-5161bdf8843b");
        supplierId = UUID.fromString("f6a7810b-98bd-4688-9cc8-b168ce4ec3b1");
        productCategoryId = UUID.fromString("ee508dd2-0589-42ba-bd72-462e63213a02");
    }

    @Test
    void testFindAll_ShouldReturnListOfProduct_WhenCalled() {
        // when
        List<Product> list = repository.findAll();

        // then
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void testFindById_ShouldReturnOptionalOfProductWithValue_WhenExist() {
        // when
        Optional<Product> product = repository.findById(productId);

        // then
        assertThat(product.isPresent()).isTrue();
    }
    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoProduct() {
        // when
        Optional<Product> product = repository.findById(UUID.randomUUID());

        // then
        assertThat(product.isEmpty()).isTrue();
    }

    @Test
    void testFindAllBySupplierIdAndProductCategoryId_ShouldReturnListOfProduct_WhenProductCategoryAndSupplierIsProvided() {
        // when
        List<Product> list = repository.findAllBySupplierIdAndProductCategoryId(supplierId, productCategoryId);

        // then
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void testFindAllBySupplierIdAndProductCategoryId_ShouldReturnListOfProduct_WhenOnlySupplierIsProvided() {
        // when
        List<Product> list = repository.findAllBySupplierIdAndProductCategoryId(supplierId, null);

        // then
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void testFindAllBySupplierIdAndProductCategoryId_ShouldReturnListOfProduct_WhenOnlyProductCategoryIsProvided() {
        // when
        List<Product> list = repository.findAllBySupplierIdAndProductCategoryId(null, productCategoryId);

        // then
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void testFindAllBySupplierIdAndProductCategoryId_ShouldReturnListOfProduct_WhenNoValueIsProvided() {
        // when
        List<Product> list = repository.findAllBySupplierIdAndProductCategoryId(null, null);

        // then
        assertThat(list.size()).isEqualTo(2);
    }
}
