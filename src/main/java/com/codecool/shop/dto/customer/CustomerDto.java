package com.codecool.shop.dto.customer;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerDto(
        UUID id,
        String name,
        String email,
        LocalDate submissionTime
) {
}
