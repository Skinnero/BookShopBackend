package com.codecool.shop.dto.supplier;

import jakarta.validation.constraints.NotBlank;

public record NewSupplierDto(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Description cannot be empty")
        String description
) {
}
