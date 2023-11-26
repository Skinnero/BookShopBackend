package com.codecool.shop.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewAddressDto(
        @NotNull(message = "Zip code cannot be empty")
        Long zipCode,
        @NotBlank(message = "City cannot be empty")
        String city,
        @NotBlank(message = "Street cannot be empty")
        String street,
        @NotBlank(message = "Street number cannot be empty")
        String streetNumber,
        String additionalInfo,
        UUID customerId
) {
}
