package com.codecool.shop.config.jwt.repository;

import com.codecool.shop.config.jwt.repository.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Query("select rt from RefreshToken rt where rt.id = :id")
    Optional<RefreshToken> findById(UUID id);
    @Query("select rt from RefreshToken rt where rt.customer.id = :customerId")
    Optional<RefreshToken> findByCustomerId(UUID customerId);
}
