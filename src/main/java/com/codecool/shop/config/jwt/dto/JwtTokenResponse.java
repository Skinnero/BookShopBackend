package com.codecool.shop.config.jwt.dto;

import java.util.UUID;

public record JwtTokenResponse(
        String type,
        String accessToken,
        UUID refreshToken,
        UUID customerId
) {
}
