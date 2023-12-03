package com.codecool.shop.dto.basket;

import java.util.List;
import java.util.UUID;

public record EditBasketDto(

        List<UUID> products
) {
}
