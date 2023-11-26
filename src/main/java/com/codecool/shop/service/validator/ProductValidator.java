package com.codecool.shop.service.validator;

import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductValidator implements Validator {
    private final ProductRepository productRepository;

    @Override
    public void validateByEntityId(UUID id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Product.class));
    }
}
