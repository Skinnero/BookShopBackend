package com.codecool.shop.service.validator;

import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductValidator implements Validator<Product> {
    private final ProductRepository productRepository;

    @Override
    public Product validateByEntityId(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Product.class));
    }
}
