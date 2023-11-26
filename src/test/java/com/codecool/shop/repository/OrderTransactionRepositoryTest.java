package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.OrderTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderTransactionRepositoryTest {
    @Autowired
    OrderTransactionRepository repository;

    private UUID orderTransactionId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        orderTransactionId = UUID.fromString("53abb680-893e-489a-84ba-0f1e5771db00");
        customerId = UUID.fromString("cbc22041-a58b-431f-8d26-7355fab4cb65");
    }

    @Test
    void testFindById_ShouldReturnOptionalOfOrderTransactionWithPresentValue_WhenExist() {
        // when
        Optional<OrderTransaction> orderTransaction = repository.findById(orderTransactionId);

        // then
        assertThat(orderTransaction.isPresent()).isTrue();
    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoOrderTransaction() {
        // when
        Optional<OrderTransaction> orderTransaction = repository.findById(UUID.randomUUID());

        // then
        assertThat(orderTransaction.isEmpty()).isTrue();
    }

    @Test
    void testFindAllByCustomerId_ShouldReturnListOfOrderTransaction_WhenCalled() {
        // when
        List<OrderTransaction> list = repository.findAllByCustomerId(customerId);

        // then
        assertThat(list.size()).isEqualTo(2);
    }
}