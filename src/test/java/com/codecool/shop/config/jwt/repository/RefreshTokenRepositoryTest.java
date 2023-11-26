package com.codecool.shop.config.jwt.repository;

import com.codecool.shop.config.jwt.repository.entity.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RefreshTokenRepositoryTest {
    @Autowired
    RefreshTokenRepository repository;

    private UUID refreshTokenId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        refreshTokenId = UUID.fromString("f81a56a8-3363-431e-b875-01185791d39f");
        customerId = UUID.fromString("ec63f4d5-6964-49a9-822c-36b7349efc3a");
    }

    @Test
    void testFindById_ShouldReturnPresentOptional_WhenExist() {
        // when
        Optional<RefreshToken> refreshToken = repository.findById(refreshTokenId);

        // then
        assertThat(refreshToken.isPresent()).isTrue();
    }

    @Test
    void testFindById_ShouldReturnEmptyOptional_WhenNoRefreshToken() {
        // when
        Optional<RefreshToken> refreshToken = repository.findById(UUID.randomUUID());

        // then
        assertThat(refreshToken.isEmpty()).isTrue();
    }

    @Test
    void testFindByCustomerId_ShouldReturnPresetOptional_WhenExist() {
        // when
        Optional<RefreshToken> refreshToken = repository.findByCustomerId(customerId);

        // then
        assertThat(refreshToken.isPresent()).isTrue();
    }

    @Test
    void testFindByCustomerId_ShouldReturnEmptyOptional_WhenNoCustomer() {
        // when
        Optional<RefreshToken> refreshToken = repository.findByCustomerId(UUID.randomUUID());

        // then
        assertThat(refreshToken.isEmpty()).isTrue();
    }



}