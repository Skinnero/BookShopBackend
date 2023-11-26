package com.codecool.shop.repository;

import com.codecool.shop.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("select prod from Product prod")
    List<Product> findAll();

    @Query("select prod from Product prod WHERE prod.id = :id")
    Optional<Product> findById(UUID id);

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM product p
                             LEFT JOIN supplier_product s ON p.id = s.product_id
                             LEFT JOIN product_category_product pc ON p.id = pc.product_id
                    WHERE
                        (cast(:productCategoryId as uuid) IS NULL OR pc.product_category_id = cast(:productCategoryId as uuid) )
                      AND
                        (cast(:supplierId as uuid) IS NULL OR s.supplier_id = cast(:supplierId as uuid) )
                        """)
    List<Product> findAllBySupplierIdAndProductCategoryId(@Param("supplierId") UUID supplierId,
                                                          @Param("productCategoryId") UUID productCategoryId);

    void deleteById(UUID id);
}
