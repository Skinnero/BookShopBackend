package com.codecool.shop.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record EditCustomerPasswordDto(
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
