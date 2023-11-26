package com.codecool.shop.dto.productcategory;

import jakarta.validation.constraints.NotBlank;

public record NewProductCategoryDto(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Department cannot be empty")
        String department
) {
}
