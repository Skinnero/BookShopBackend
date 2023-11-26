package com.codecool.shop.config.jwt.repository.entity;

import com.codecool.shop.repository.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private CustomerRole customerRole;
    @ManyToMany(mappedBy = "roles")
    private final Set<Customer> customers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(id, role.id) &&
                Objects.equals(customerRole, role.customerRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerRole);
    }

    public void assignCustomerToRole(Customer customer) {
        customers.add(customer);
        customer.getRoles().add(this);
    }
}
