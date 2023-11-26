package com.codecool.shop.config.jwt.service.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RefreshTokenExpiredExceptionTest {
    @Test
    void testRefreshTokenExpiredException() {
        UUID tokenId = UUID.randomUUID();

        try {
            throw new RefreshTokenExpiredException(tokenId);
        } catch (RefreshTokenExpiredException ex) {
            assertThat(ex.getMessage()).isEqualTo("Refresh token " + tokenId + " has expired");
        }
    }
}