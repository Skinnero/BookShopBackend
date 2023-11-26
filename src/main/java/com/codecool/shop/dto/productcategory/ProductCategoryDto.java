package com.codecool.shop.dto.productcategory;

import java.util.UUID;

public record ProductCategoryDto(
        UUID id,
        String name,
        String department
) {
}
