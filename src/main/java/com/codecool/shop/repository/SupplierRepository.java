package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    @Query("select s from Supplier s left join fetch s.products ")
    List<Supplier> findAll();

    @Query("SELECT s FROM Supplier s left join fetch s.products WHERE s.id = :id")
    Optional<Supplier> findById(UUID id);

    void deleteById(UUID id);
}
