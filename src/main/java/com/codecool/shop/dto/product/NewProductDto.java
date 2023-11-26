package com.codecool.shop.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NewProductDto(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Description cannot be empty")
        String description,
        @NotNull(message = "Price cannot be empty")
        BigDecimal price,
        @NotBlank(message = "Currency cannot be empty")
        String currency
) {
}
