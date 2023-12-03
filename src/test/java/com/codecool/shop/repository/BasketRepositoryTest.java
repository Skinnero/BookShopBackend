package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Basket;
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
    void testFindById_ShouldReturnPresentOptional_WhenBasketExist() {
        // when
        Optional<Basket> basket = repository.findById(basketId);

        // then
        assertThat(basket.isPresent()).isTrue();
    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoBasket() {
        // when
        Optional<Basket> basket = repository.findById(UUID.randomUUID());

        // then
        assertThat(basket.isEmpty()).isTrue();
    }

    @Test
    void testFindByCustomerId_ShouldReturnPresentOptional_WhenBasketExist() {
        // when
        Optional<Basket> basket = repository.findByCustomerId(customerId);

        // then
        assertThat(basket.isPresent()).isTrue();
    }

    @Test
    void testFindByCustomerId_ShouldReturnEmptyOptional_WhenNoBasket() {
        // when
        Optional<Basket> address = repository.findByCustomerId(UUID.randomUUID());

        // then
        assertThat(address.isEmpty()).isTrue();
    }
    @Test
    void testFindAllByCustomerId_ShouldReturnEmptyOptional_WhenNoBasket() {
        // when
        List<Basket> basketList = repository.findAllByCustomerId(customerId);

        // then
        assertThat(basketList.size()).isEqualTo(2);
    }

}
