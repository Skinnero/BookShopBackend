package com.codecool.shop.config.jwt.dto;

import java.util.UUID;

public record RefreshTokenResponse(
        String type,
        UUID refreshToken,
        String accessToken
) {
}
