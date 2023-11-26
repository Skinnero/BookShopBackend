package com.codecool.shop.config.jwt.repository.entity;

import com.codecool.shop.repository.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {
    private Role role;
    private Customer customer;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(UUID.randomUUID());
        role.setCustomerRole(CustomerRole.ROLE_USER);

        customer = new Customer();
        customer.setId(UUID.randomUUID());
    }

    @Test
    void testEquals_ShouldReturnTrue_WhenObjectsHaveSameValues() {
        // given
        Role otherRole = new Role(role.getId(), role.getCustomerRole());

        // then
        assertThat(role).isEqualTo(otherRole);
        assertThat(role.hashCode()).isEqualTo(otherRole.hashCode());
    }

    @Test
    void testEquals_ShouldReturnFalse_WhenObjectsHaveDifferentValues() {
        // given
        Role otherRole = new Role(UUID.randomUUID(), role.getCustomerRole());

        // then
        assertThat(role).isNotEqualTo(otherRole);
        assertThat(role.hashCode()).isNotEqualTo(otherRole.hashCode());
    }

    @Test
    void testAssignCustomerToRole_ShouldAddToCollection_WhenIsNotThere() {
        // when
        role.assignCustomerToRole(customer);

        // then
        assertThat(role.getCustomers().size()).isEqualTo(1);
        assertThat(customer.getRoles().size()).isEqualTo(1);

    }

    @Test
    void testAssignCustomerToRole_ShouldNotAddToCollection_WhenIsAlreadyThere() {
        // when
        role.assignCustomerToRole(customer);
        role.assignCustomerToRole(customer);

        // then
        assertThat(role.getCustomers().size()).isEqualTo(1);
        assertThat(customer.getRoles().size()).isEqualTo(1);
    }

}
