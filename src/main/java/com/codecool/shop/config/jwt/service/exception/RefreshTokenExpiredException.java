package com.codecool.shop.config.jwt.service.exception;

import java.util.UUID;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(UUID tokenId) {
        super(String.format("Refresh token %s has expired", tokenId));
    }
}
