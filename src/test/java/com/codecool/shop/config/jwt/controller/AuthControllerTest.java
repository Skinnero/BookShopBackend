package com.codecool.shop.config.jwt.controller;

import com.codecool.shop.config.jwt.JwtUtils;
import com.codecool.shop.config.jwt.dto.RefreshTokenRequest;
import com.codecool.shop.config.jwt.dto.RefreshTokenResponse;
import com.codecool.shop.config.jwt.repository.entity.RefreshToken;
import com.codecool.shop.config.jwt.service.RefreshTokenService;
import com.codecool.shop.config.jwt.service.exception.RefreshTokenExpiredException;
import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.CustomerService;
import com.codecool.shop.service.exception.EmailNotFoundException;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    CustomerService customerService;
    @MockBean
    RefreshTokenService refreshTokenService;
    @MockBean
    JwtUtils jwtUtils;
    @MockBean
    CustomerRepository customerRepository;

    private Authentication authentication;
    private UserDetails userDetails;
    private String email;
    private UUID refreshTokenId;
    private UUID customerId;
    private String jwtToken;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        email = "Email@wp.pl";
        authentication = Mockito.mock(Authentication.class);
        userDetails = Mockito.mock(UserDetails.class);
        refreshTokenId = UUID.fromString("16d27fa8-cfa3-46db-a0dd-3683f569e471");
        customerId = UUID.fromString("a5a0ca84-d118-45b7-8085-1a41b849a336");

        jwtToken = "accessToken";

        customerDto = new CustomerDto(
                customerId,
                null,
                null,
                null
        );
    }

    @Test
    void testLoginCustomer_ShouldReturnStatusCreatedAndJwtTokenResponse_WhenCustomerExist() throws Exception {
        // given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(refreshTokenId);

        String contentRequest = """
                {
                    "email": "Email@wp.pl",
                    "password": "password"
                }
                """;

        String contentResponse = """
                {
                    "type": "Bearer",
                    "accessToken": "accessToken",
                    "refreshToken": "16d27fa8-cfa3-46db-a0dd-3683f569e471",
                    "customerId": "a5a0ca84-d118-45b7-8085-1a41b849a336"
                }
                """;
        // when
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn(email);
        Mockito.when(customerService.getCustomerByEmail(email)).thenReturn(customerDto);
        Mockito.when(jwtUtils.generateTokenFromAuthentication(any(Authentication.class))).thenReturn("accessToken");
        Mockito.when(refreshTokenService.createRefreshToken(customerId)).thenReturn(refreshToken);

        // then
        mockMvc.perform(post("/api/v1/auths/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isCreated(),
                        content().json(contentResponse)
                );

    }

    @Test
    void testLoginCustomer_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValuesInBody() throws Exception {
        // given
        String contentRequest = """
                {
                    "email": "",
                    "password": ""
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/auths/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage", Matchers.containsString("Email cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Password cannot be empty"))
                );

    }

    @Test
    void testLoginCustomer_ShouldReturnStatusNotFoundAndErrorMessages_WhenNoCustomer() throws Exception {
        // given
        String contentRequest = """
                {
                    "email": "Email@wp.pl",
                    "password": "password"
                }
                """;

        // when
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn(email);
        Mockito.when(customerService.getCustomerByEmail(email))
                .thenThrow(new EmailNotFoundException(email));

        // then
        mockMvc.perform(post("/api/v1/auths/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Email " + email + " cannot be found")
                );
    }

    @Test
    void testSignUpCustomer_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Kacper",
                    "email": "Email@wp.pl",
                    "password": "password"
                }
                """;
        // when
        Mockito.when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(post("/api/v1/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideEmailAndResponseForCustomerValidation")
    void testSignUpCustomer_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues(
            String emailRequest, String emailResponse) throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "email": "%s",
                    "password": ""
                }
                """.formatted(emailRequest);

        // when
        Mockito.when(customerRepository.findByEmail(email)).thenReturn(Optional.of(new Customer()));

        // then
        mockMvc.perform(post("/api/v1/auths/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Password cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString(emailResponse))
                );
    }

    private static Stream<Arguments> provideEmailAndResponseForCustomerValidation() {
        return Stream.of(
                Arguments.of("", "Email cannot be empty"),
                Arguments.of("email", "Email is invalid"),
                Arguments.of("Email@wp.pl", "Email is already taken")
        );
    }

    @Test
    void testRefreshToken_ShouldReturnStatusAcceptedAndRefreshTokenResponse_WhenValidJwtTokenAndRefreshToken()
            throws Exception {
        // given
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(
                "Bearer",
                refreshTokenId,
                jwtToken
        );

        String contentRequest = """
                {
                    "refreshToken": "16d27fa8-cfa3-46db-a0dd-3683f569e471"
                }
                """;

        String contentResponse = """
                {
                    "refreshToken": "16d27fa8-cfa3-46db-a0dd-3683f569e471",
                    "accessToken": "accessToken"
                }
                """;

        // when
        Mockito.when(jwtUtils.parseToken(any(HttpServletRequest.class))).thenReturn(jwtToken);
        Mockito.when(jwtUtils.hasTokenExpired(jwtToken)).thenReturn(true);
        Mockito.when(refreshTokenService.handleRefreshTokenRequest(any(RefreshTokenRequest.class)))
                .thenReturn(refreshTokenResponse);

        // then
        mockMvc.perform(post("/api/v1/auths/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isAccepted(),
                        content().json(contentResponse)
                );
    }


    @Test
    void testRefreshToken_ShouldReturnStatusNotFoundAndErrorMessage_WhenValidJwtTokenAndRefreshTokenNotExist()
            throws Exception {
        // given
        String contentRequest = """
                {
                    "refreshToken": "16d27fa8-cfa3-46db-a0dd-3683f569e471"
                }
                """;

        // when
        Mockito.when(jwtUtils.parseToken(any(HttpServletRequest.class))).thenReturn(jwtToken);
        Mockito.when(jwtUtils.hasTokenExpired(jwtToken)).thenReturn(true);
        Mockito.when(refreshTokenService.handleRefreshTokenRequest(any(RefreshTokenRequest.class)))
                .thenThrow(new ObjectNotFoundException(refreshTokenId, RefreshToken.class));

        // then
        mockMvc.perform(post("/api/v1/auths/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + RefreshToken.class.getSimpleName() +
                                        " and id " + refreshTokenId + " does not exist")
                );
    }

    @Test
    void testRefreshToken_ShouldReturnStatusNotFoundAndErrorMessage_WhenValidJwtTokenAndRefreshTokenExpired()
            throws Exception {
        // given
        String contentRequest = """
                {
                    "refreshToken": "16d27fa8-cfa3-46db-a0dd-3683f569e471"
                }
                """;

        // when
        Mockito.when(jwtUtils.parseToken(any(HttpServletRequest.class))).thenReturn(jwtToken);
        Mockito.when(jwtUtils.hasTokenExpired(jwtToken)).thenReturn(true);
        Mockito.when(refreshTokenService.handleRefreshTokenRequest(any(RefreshTokenRequest.class)))
                .thenThrow(new RefreshTokenExpiredException(refreshTokenId));

        // then
        mockMvc.perform(post("/api/v1/auths/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage")
                                .value("Refresh token " + refreshTokenId + " has expired")
                );
    }

    @Test
    void testLogoutCustomer_ShouldReturnStatusNoContent_WhenJwtTokenIsValid() throws Exception {
        // when
        Mockito.when(jwtUtils.parseToken(any(HttpServletRequest.class))).thenReturn(jwtToken);
        Mockito.when(jwtUtils.hasTokenExpired(jwtToken)).thenReturn(true);
        Mockito.when(jwtUtils.getEmailFromToken(jwtToken)).thenReturn(email);
        Mockito.when(customerService.getCustomerByEmail(email)).thenReturn(customerDto);

        // then
        mockMvc.perform(post("/api/v1/auths/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testLogoutCustomer_ShouldReturnStatusUnauthorized_WhenJwtTokenIsInvalid() throws Exception {
        // when
        Mockito.when(jwtUtils.parseToken(any(HttpServletRequest.class))).thenReturn("");

        // then
        mockMvc.perform(post("/api/v1/auths/logout"))
                .andExpect(status().isUnauthorized());
    }
}