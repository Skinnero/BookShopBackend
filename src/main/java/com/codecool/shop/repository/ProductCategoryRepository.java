package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    @Query("select pc from ProductCategory pc left join fetch pc.products ")
    List<ProductCategory> findAll();
    @Query("SELECT pc FROM ProductCategory pc left join fetch pc.products WHERE pc.id = :id")
    Optional<ProductCategory> findById(UUID id);

    void deleteById(UUID id);
}
