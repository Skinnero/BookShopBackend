package com.codecool.shop.repository.entity;

import com.codecool.shop.config.jwt.repository.entity.CustomerRole;
import com.codecool.shop.config.jwt.repository.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {
    private Customer customer;
    private Role role;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                UUID.randomUUID(),
                "Name",
                "Email@wp.pl",
                LocalDate.of(11,11, 11),
                "Password",
                false
        );

        role = new Role();
        role.setId(UUID.randomUUID());
        role.setCustomerRole(CustomerRole.ROLE_USER);
    }

    @Test
    void testEquals_ShouldReturnTrue_WhenObjectsHaveSameValues() {
        // given
        Customer otherCustomer = new Customer(
                customer.getId(),
                "Name",
                "Email@wp.pl",
                LocalDate.of(11,11, 11),
                "Password",
                false
        );

        // then
        assertThat(customer).isEqualTo(otherCustomer);
        assertThat(customer.hashCode()).isEqualTo(otherCustomer.hashCode());
    }

    @Test
    void testEquals_ShouldReturnFalse_WhenObjectsHaveDifferentValues() {
        // given
        Customer otherCustomer = new Customer(
                UUID.randomUUID(),
                "Name",
                "Email@wp.pl",
                LocalDate.of(11,11, 11),
                "Password",
                false
        );

        // then
        assertThat(customer).isNotEqualTo(otherCustomer);
        assertThat(customer.hashCode()).isNotEqualTo(otherCustomer.hashCode());
    }

    @Test
    void testAssignCustomerToRole_ShouldAddToCollection_WhenIsNotThere() {
        // when
        customer.assignRoleToCustomer(role);

        // then
        assertThat(role.getCustomers().size()).isEqualTo(1);
        assertThat(customer.getRoles().size()).isEqualTo(1);

    }

    @Test
    void testAssignCustomerToRole_ShouldNotAddToCollection_WhenIsAlreadyThere() {
        // when
        customer.assignRoleToCustomer(role);
        customer.assignRoleToCustomer(role);

        // then
        assertThat(role.getCustomers().size()).isEqualTo(1);
        assertThat(customer.getRoles().size()).isEqualTo(1);
    }

}
