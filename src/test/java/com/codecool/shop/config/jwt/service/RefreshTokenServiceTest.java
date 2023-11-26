package com.codecool.shop.config.jwt.service;

import com.codecool.shop.config.jwt.JwtUtils;
import com.codecool.shop.config.jwt.dto.RefreshTokenRequest;
import com.codecool.shop.config.jwt.dto.RefreshTokenResponse;
import com.codecool.shop.config.jwt.repository.RefreshTokenRepository;
import com.codecool.shop.config.jwt.repository.entity.RefreshToken;
import com.codecool.shop.config.jwt.service.exception.RefreshTokenExpiredException;
import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    @InjectMocks
    RefreshTokenService service;
    @Mock
    RefreshTokenRepository repository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    JwtUtils jwtUtils;

    private UUID customerId;
    private UUID refreshTokenId;
    private RefreshToken refreshToken;
    private Customer customer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "refreshTokenDurationMs", 2_629_800_000L);

        customerId = UUID.randomUUID();
        refreshTokenId = UUID.randomUUID();

        customer = new Customer();
        customer.setId(customerId);
        customer.setEmail("Email@wp.pl");

        refreshToken = new RefreshToken();
        refreshToken.setId(refreshTokenId);
        refreshToken.setExpiryDate(Instant.now().plusMillis(2_629_800_000L));
        refreshToken.setCustomer(customer);
    }

    @Test
    void testCreateRefreshToken_ShouldReturnRefreshToken_WhenCustomerExist() {
        // when
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(repository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        RefreshToken expectedRefreshToken = service.createRefreshToken(customerId);

        // then
        assertThat(refreshToken).isNotEqualTo(expectedRefreshToken);
    }

    @Test
    void testCreateRefreshToken_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.createRefreshToken(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testDeleteRefreshTokenByCustomerId_ShouldDeleteToken_WhenExist() {
        // when
        Mockito.when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(refreshToken));
        service.deleteRefreshTokenByCustomerId(customerId);

        // then
        Mockito.verify(repository, Mockito.times(1)).delete(refreshToken);
    }

    @Test
    void testDeleteRefreshTokenByCustomerId_ShouldDoNothing_WhenNoCustomer() {
        // when
        Mockito.when(repository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        service.deleteRefreshTokenByCustomerId(customerId);

        // then
        Mockito.verify(repository, Mockito.times(0)).delete(any());
    }

    @Test
    void testHandleRefreshTokenRequest_ShouldReturnRefreshTokenResponse_WhenRefreshTokenExistAndNotExpired() {
        // given
        String accessToken = "accessToken";

        // when
        Mockito.when(repository.findById(refreshTokenId)).thenReturn(Optional.of(refreshToken));
        Mockito.when(repository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(jwtUtils.generateTokenFromEmail(anyString())).thenReturn(accessToken);
        RefreshTokenResponse expectedRefreshTokenResponse = service.handleRefreshTokenRequest(
                new RefreshTokenRequest(refreshTokenId));

        // then
        assertThat(expectedRefreshTokenResponse.refreshToken()).isEqualTo(refreshTokenId);
        assertThat(expectedRefreshTokenResponse.accessToken()).isEqualTo(accessToken);
    }

    @Test
    void testHandleRefreshTokenRequest_ShouldThrowRefreshTokenExpiredException_WhenRefreshTokenExistAndExpired() {
        // given
        refreshToken.setExpiryDate(Instant.now());

        // when
        Mockito.when(repository.findById(refreshTokenId)).thenReturn(Optional.of(refreshToken));

        // then
        assertThatThrownBy(() -> service.handleRefreshTokenRequest(new RefreshTokenRequest(refreshTokenId)))
                .isInstanceOf(RefreshTokenExpiredException.class);
    }

    @Test
    void testHandleRefreshTokenRequest_ShouldThrowObjectNotFoundException_WhenNoRefreshToken() {
        // when
        Mockito.when(repository.findById(refreshTokenId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.handleRefreshTokenRequest(new RefreshTokenRequest(refreshTokenId)))
                .isInstanceOf(ObjectNotFoundException.class);
    }


}