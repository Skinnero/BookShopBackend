package com.codecool.shop.config.jwt.dto;

import java.util.UUID;

public record RefreshTokenResponse(
        UUID refreshToken,
        String accessToken
) {
}
