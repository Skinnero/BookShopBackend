package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, UUID> {
    @Query("SELECT ot FROM OrderTransaction ot WHERE ot.id = :id")
    Optional<OrderTransaction> findById(UUID id);

    @Query("select ot from OrderTransaction ot where ot.customer.id = :customerId")
    List<OrderTransaction> findAllByCustomerId(UUID customerId);
}
