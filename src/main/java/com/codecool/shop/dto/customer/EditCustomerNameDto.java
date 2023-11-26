package com.codecool.shop.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record EditCustomerNameDto(
        @NotBlank(message = "Name cannot be empty")
        String name
) {
}
