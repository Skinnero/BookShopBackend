package com.codecool.shop.dto.basket;

import jakarta.validation.Valid;

import java.util.List;

public record EditBasketDto(
        @Valid
        List<ProductsInBasketDto> products
) {
}
