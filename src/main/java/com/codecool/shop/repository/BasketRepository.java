package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BasketRepository extends JpaRepository<Basket, UUID> {
    @Query("select b from Basket b left join fetch b.products where b.id = :id")
    Optional<Basket> findById(UUID id);

    @Query("select b from Basket b left join fetch b.products where b.customer.id = :customerId and b.isComplete = false")
    Optional<Basket> findByCustomerId(UUID customerId);

    @Query("select b from Basket b left join fetch b.products where b.customer.id = :customerId")
    List<Basket> findAllByCustomerId(UUID customerId);

    void deleteById(UUID id);
}
