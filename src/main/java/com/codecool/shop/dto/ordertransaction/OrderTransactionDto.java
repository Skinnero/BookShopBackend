package com.codecool.shop.dto.ordertransaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderTransactionDto(
        UUID id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime submissionTime,
        UUID basketId,
        UUID customerId
) {
}
