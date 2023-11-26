package com.codecool.shop.dto.basket;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductsInBasketDto(
        UUID productId,
        @NotNull(message = "Quantity cannot be null")
        Integer quantity
) {
}
