package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AddressRepositoryTest {
    @Autowired
    AddressRepository repository;

    private UUID addressId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        addressId = UUID.fromString("a5ad0a45-d3c9-4867-9b06-6e6d27d20e2d");
        customerId = UUID.fromString("ec63f4d5-6964-49a9-822c-36b7349efc3a");
    }

    @Test
    void testFindById_ShouldReturnOptionalOfAddressIsPresent_WhenExist() {
        // when
        Optional<Address> address = repository.findById(addressId);

        // then
        assertThat(address.isPresent()).isTrue();

    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoAddress() {
        // when
        Optional<Address> address = repository.findById(UUID.randomUUID());

        // then
        assertThat(address.isEmpty()).isTrue();
    }

    @Test
    void testFindByCustomerId_ShouldReturnEmptyOptional_WhenNoAddressWithExactCustomerId() {
        // when
        Optional<Address> address = repository.findByCustomerId(UUID.randomUUID());

        // then
        assertThat(address.isEmpty()).isTrue();
    }
    @Test
    void testFindByCustomerId_ShouldReturnOptionalOfAddressIsPresent_WhenExistWithExactCustomerId() {
        // when
        Optional<Address> address = repository.findByCustomerId(customerId);

        // then
        assertThat(address.isPresent()).isTrue();
    }
}
