package com.codecool.shop.config.jwt.controller;

import com.codecool.shop.config.jwt.JwtUtils;
import com.codecool.shop.config.jwt.dto.JwtTokenResponse;
import com.codecool.shop.config.jwt.dto.LoginRequest;
import com.codecool.shop.config.jwt.dto.RefreshTokenRequest;
import com.codecool.shop.config.jwt.dto.RefreshTokenResponse;
import com.codecool.shop.config.jwt.repository.entity.RefreshToken;
import com.codecool.shop.config.jwt.service.RefreshTokenService;
import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.dto.customer.NewCustomerDto;
import com.codecool.shop.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auths")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping(value = "/sign-in", produces = "application/json")
    private ResponseEntity<JwtTokenResponse> signInCustomer(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CustomerDto customerDto = customerService.getCustomerByEmail(userDetails.getUsername());

        String token = jwtUtils.generateTokenFromAuthentication(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customerDto.id());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtTokenResponse(
                        "Bearer",
                        token,
                        refreshToken.getId(),
                        customerDto.id()
                )
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpCustomer(@Valid @RequestBody NewCustomerDto newCustomerDto) {
        customerService.saveCustomer(newCustomerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    private ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletRequest request) {

        String token = jwtUtils.parseToken(request);
        if (!token.isEmpty() && jwtUtils.hasTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(refreshTokenService.handleRefreshTokenRequest(refreshTokenRequest));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutCustomer(HttpServletRequest request) {

        String token = jwtUtils.parseToken(request);
        if (token.isEmpty() || !jwtUtils.hasTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomerDto customerDto = customerService.getCustomerByEmail(jwtUtils.getEmailFromToken(token));
        refreshTokenService.deleteRefreshTokenByCustomerId(customerDto.id());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
