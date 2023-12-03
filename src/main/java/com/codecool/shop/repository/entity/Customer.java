package com.codecool.shop.repository.entity;

import com.codecool.shop.config.jwt.repository.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Email(message = "Email is invalid")
    private String email;
    private LocalDate submissionTime;
    private String password;
    private Boolean isDeleted = false;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "customer_role",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private final Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id, customer.id) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(submissionTime, customer.submissionTime) &&
                Objects.equals(password, customer.password) &&
                Objects.equals(isDeleted, customer.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, submissionTime, password, isDeleted);
    }

    public void assignRoleToCustomer(Role role) {
        roles.add(role);
        role.getCustomers().add(this);
    }
}
