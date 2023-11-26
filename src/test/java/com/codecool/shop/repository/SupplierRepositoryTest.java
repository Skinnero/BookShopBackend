package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SupplierRepositoryTest {
    @Autowired
    SupplierRepository repository;


    public UUID supplierId;
    @BeforeEach
    void setUp() {
        supplierId = UUID.fromString("f6a7810b-98bd-4688-9cc8-b168ce4ec3b1");
    }

    @Test
    void testFindAll_ShouldReturnListOfSupplier_WhenCalled() {
        // when
        List<Supplier> list = repository.findAll();

        // then
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void testFindById_ShouldReturnOptionalOfSupplierWithPresentValue_WhenExist() {
        // when
        Optional<Supplier> supplier = repository.findById(supplierId);

        // then
        assertThat(supplier.isPresent()).isTrue();
    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoSupplier() {
        // when
        Optional<Supplier> supplier = repository.findById(UUID.randomUUID());

        // then
        assertThat(supplier.isEmpty()).isTrue();
    }
}
