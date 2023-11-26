package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductCategoryRepositoryTest {
    @Autowired
    ProductCategoryRepository repository;

    private UUID productCategoryId;
    @BeforeEach
    void setUp() {
        productCategoryId = UUID.fromString("ee508dd2-0589-42ba-bd72-462e63213a02");
    }

    @Test
    void testFindAll_ShouldReturnListOfProductCategories_WhenCalled() {
        // when
        List<ProductCategory> list = repository.findAll();

        // then
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void testFindById_ShouldReturnOptionalOfProductCategoriesWithPresentValues_WhenExist() {
        // when
        Optional<ProductCategory> productCategory = repository.findById(productCategoryId);

        // then
        assertThat(productCategory.isPresent()).isTrue();
    }
    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoProductCategory() {
        // when
        Optional<ProductCategory> productCategory = repository.findById(UUID.randomUUID());

        // then
        assertThat(productCategory.isEmpty()).isTrue();
    }
}
