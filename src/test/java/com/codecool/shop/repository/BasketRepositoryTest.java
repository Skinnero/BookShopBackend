package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.projection.BasketProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BasketRepositoryTest {
    @Autowired
    BasketRepository repository;

    private UUID basketId;
    private UUID customerId;
    
    @BeforeEach
    void setUp() {
        basketId = UUID.fromString("39332501-f73c-4788-a690-bd05364870c0");
        customerId = UUID.fromString("cbc22041-a58b-431f-8d26-7355fab4cb65");
    }

    @Test
    void testFindById_ShouldReturnPresentOptional_WhenAddressExist() {
        // when
        Optional<Basket> address = repository.findById(basketId);

        // then
        assertThat(address.isPresent()).isTrue();
    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoAddress() {
        // when
        Optional<Basket> address = repository.findById(UUID.randomUUID());

        // then
        assertThat(address.isEmpty()).isTrue();
    }

    @Test
    void testFindProductsInBasketById_ShouldReturnListOfCasketProjection_WhenCalled() {
        // when
        List<BasketProjection> list = repository.findProductsInBasketById(basketId);

        // then
        assertThat(list.size()).isEqualTo(1);

    }
    @Test
    void testFindProductsInBasketByCustomerId_ShouldReturnListOfCasketProjection_WhenCalled() {
        // when
        List<BasketProjection> list = repository.findProductsInBasketByCustomerId(customerId);

        // then
        assertThat(list.size()).isEqualTo(1);
    }
}
