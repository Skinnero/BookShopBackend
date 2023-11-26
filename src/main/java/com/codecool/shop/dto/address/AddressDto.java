package com.codecool.shop.dto.address;

import java.util.UUID;

public record AddressDto(
        UUID id,
        Long zipCode,
        String city,
        String street,
        String streetNumber,
        String additionalInfo
) {
}
