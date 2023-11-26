package com.codecool.shop.dto.supplier;

import java.util.UUID;

public record SupplierDto(
        UUID id,
        String name,
        String description
) {
}
