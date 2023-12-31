package com.codecool.shop.service.validator;

import com.codecool.shop.repository.ProductCategoryRepository;
import com.codecool.shop.repository.entity.ProductCategory;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductCategoryValidator implements Validator {
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void validateByEntityId(UUID id) {
        productCategoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ProductCategory.class));
    }
}
