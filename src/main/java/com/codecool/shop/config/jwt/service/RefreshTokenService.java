package com.codecool.shop.config.jwt.service;

import com.codecool.shop.config.jwt.JwtUtils;
import com.codecool.shop.config.jwt.dto.RefreshTokenRequest;
import com.codecool.shop.config.jwt.dto.RefreshTokenResponse;
import com.codecool.shop.config.jwt.repository.RefreshTokenRepository;
import com.codecool.shop.config.jwt.repository.entity.RefreshToken;
import com.codecool.shop.config.jwt.service.exception.RefreshTokenExpiredException;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerValidator customerValidator;
    private final JwtUtils jwtUtils;
    @Value("${shop.app.jwtRefreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    public RefreshToken createRefreshToken(UUID customerId) {
        deleteRefreshTokenByCustomerId(customerId);

        logger.info("Creating new refresh token");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCustomer(customerValidator.validateByEntityId(customerId));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenResponse handleRefreshTokenRequest(RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenRepository.findById(refreshTokenRequest.refreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getCustomer)
                .map(this::createRefreshTokenResponse)
                .orElseThrow(() -> new ObjectNotFoundException(refreshTokenRequest.refreshToken(), RefreshToken.class));
    }

    public void deleteRefreshTokenByCustomerId(UUID customerId) {
        refreshTokenRepository.findByCustomerId(customerId).ifPresent(refreshTokenRepository::delete);
    }

    private RefreshTokenResponse createRefreshTokenResponse(Customer customer) {
        RefreshToken newRefreshToken = createRefreshToken(customer.getId());
        logger.info("Refresh Token has been created");
        return new RefreshTokenResponse(newRefreshToken.getId(),
                jwtUtils.generateTokenFromEmail(customer.getEmail()));
    }

    private RefreshToken verifyExpiration(RefreshToken refreshToken) {
        logger.info("Verifying refresh token");
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            logger.error("Refresh token has expired");
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredException(refreshToken.getId());
        }
        logger.info("Refresh token verified");
        return refreshToken;
    }

}
