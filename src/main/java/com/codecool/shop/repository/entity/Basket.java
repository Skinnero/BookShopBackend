package com.codecool.shop.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean isComplete = false;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToMany
    @JoinTable(name = "basket_product",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    public void appendProduct(Product product) {
        products.add(product);
    }

    public void removeProducts() {
        products.clear();
    }
}
