package com.codecool.shop.dto.basket;

import java.util.List;
import java.util.UUID;

public record BasketWithProductsDto(
        UUID id,
        List<ProductsInBasketDto> products
) {
}
