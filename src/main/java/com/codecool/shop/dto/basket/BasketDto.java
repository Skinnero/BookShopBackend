package com.codecool.shop.dto.basket;

import com.codecool.shop.dto.product.ProductDto;

import java.util.List;
import java.util.UUID;

public record BasketDto (
        UUID basketId,
        List<ProductDto> products
) {
}
