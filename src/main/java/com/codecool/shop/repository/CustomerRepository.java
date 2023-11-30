package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("SELECT cu FROM Customer cu join fetch cu.roles WHERE cu.id = :id and cu.isDeleted = false")
    Optional<Customer> findById(UUID id);

    @Query("select cu from Customer cu join fetch cu.roles where cu.email = :email and cu.isDeleted = false ")
    Optional<Customer> findByEmail(String email);
}
