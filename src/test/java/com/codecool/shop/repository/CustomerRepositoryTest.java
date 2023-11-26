package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    CustomerRepository repository;

    private UUID customerId;
    private String email;

    @BeforeEach
    void setUp() {
        customerId = UUID.fromString("ec63f4d5-6964-49a9-822c-36b7349efc3a");
        email = "mati@wp.pl";
    }

    @Test
    void testFindById_ShouldReturnOptionalOfCustomerWithPresentValue_WhenExistAndNotDeleted() {
        // when
        Optional<Customer> customer = repository.findById(customerId);

        // then
        assertThat(customer.isPresent()).isTrue();
    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoCustomerOrDeleted() {
        // when
        Optional<Customer> customer = repository.findById(UUID.randomUUID());

        // then
        assertThat(customer.isEmpty()).isTrue();
    }

    @Test
    void testFindByEmail_ShouldReturnEmptyOptional_WhenNoCustomerOrDeleted() {
        // when
        Optional<Customer> customer = repository.findByEmail("");

        // then
        assertThat(customer.isEmpty()).isTrue();
    }

    @Test
    void testFindByEmail_ShouldReturnOptionalOfCustomerWithPresentValue_WhenExistAndDeleted() {
        // when
        Optional<Customer> customer = repository.findByEmail(email);

        // then
        assertThat(customer.isPresent()).isTrue();
    }
}
