package com.oms.auth.service;

import com.oms.auth.dto.*;
import com.oms.auth.exception.InvalidCredentialsException;
import com.oms.auth.exception.InvalidRefreshTokenException;
import com.oms.auth.model.RefreshToken;
import com.oms.auth.repository.RefreshTokenRepository;
import com.oms.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oms.auth.model.User;
import com.oms.auth.exception.ResourceAlreadyExistsException;
import com.oms.auth.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;

    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(repository.existsByUsername(request.getUsername())) {

            throw new ResourceAlreadyExistsException(
                    "Username already exists");

        }

        if(repository.existsByEmail(request.getEmail())) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists");

        }

        User user = User.builder()

                .username(request.getUsername())

                .email(request.getEmail())

                .password(passwordEncoder.encode(request.getPassword()))

                .role(request.getRole())

                .enabled(true)

                .build();

        repository.save(user);

        return RegisterResponse.builder()

                .message("User Registered Successfully")

                .build();

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getUsername(),

                        request.getPassword()

                )

        );

        User user = repository.findByUsername(request.getUsername())

                .orElseThrow(() ->

                        new InvalidCredentialsException(

                                "Invalid Username or Password"

                        )

                );

        return buildLoginResponse(user);

    }

    private LoginResponse buildLoginResponse(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", user.getRole().name());

        claims.put("email", user.getEmail());

        String accessToken =

                jwtService.generateAccessToken(

                        user.getUsername(),

                        claims

                );

        String refreshToken =

                jwtService.generateRefreshToken(

                        user.getUsername()

                );

        saveRefreshToken(refreshToken, user);

        return LoginResponse.builder()

                .accessToken(accessToken)

                .refreshToken(refreshToken)

                .tokenType("Bearer")

                .expiresIn(3600L)

                .username(user.getUsername())

                .role(user.getRole().name())

                .build();

    }

    private void saveRefreshToken(

            String token,

            User user) {

        RefreshToken refreshToken =

                RefreshToken.builder()

                        .token(token)

                        .expiryDate(

                                LocalDateTime.now()

                                        .plusDays(7)

                        )

                        .user(user)

                        .build();

        refreshTokenRepository.save(refreshToken);

    }

    @Override
    public RefreshTokenResponse refreshToken(
            RefreshTokenRequest request) {

        RefreshToken refreshToken =

                refreshTokenRepository

                        .findByToken(request.getRefreshToken())

                        .orElseThrow(() ->

                                new InvalidRefreshTokenException(
                                        "Refresh Token Not Found"));

        if(refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new InvalidRefreshTokenException(
                    "Refresh Token Expired");

        }

        User user = refreshToken.getUser();

        String newAccessToken =
                jwtService.generateAccessToken(user);

        return RefreshTokenResponse.builder()

                .accessToken(newAccessToken)

                .refreshToken(request.getRefreshToken())

                .tokenType("Bearer")

                .expiresIn(3600L)

                .build();

    }

}