package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.projection.BasketProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BasketRepository extends JpaRepository<Basket, UUID> {
    @Query("select b from Basket b where b.id = :id")
    Optional<Basket> findById(UUID id);

    @Query(nativeQuery = true, value = """
            select id as basketId, p.product_id as productId, count(p.product_id) as quantity from basket
            left join basket_product as p on id = p.basket_id
            where id = :id and is_complete = false
            group by id, p.product_id
            """)
    List<BasketProjection> findProductsInBasketById(UUID id);

    @Query(nativeQuery = true, value = """
            select id as basketId, p.product_id as productId, count(p.product_id) as quantity from basket
            left join basket_product as p on id = p.basket_id
            where basket.customer_id = :id and is_complete = false
            group by id, p.product_id
            """)
    List<BasketProjection> findProductsInBasketByCustomerId(UUID id);
}
