package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("select ad from Address ad where ad.id = :id")
    Optional<Address> findById(UUID id);

    @Query("select ad from Address ad where ad.customer.id = :customerId")
    Optional<Address> findByCustomerId(UUID customerId);
}
