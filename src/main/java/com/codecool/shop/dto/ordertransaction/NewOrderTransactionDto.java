package com.codecool.shop.dto.ordertransaction;

import java.util.UUID;

public record NewOrderTransactionDto(
        UUID basketId,
        UUID customerId
) {
}
