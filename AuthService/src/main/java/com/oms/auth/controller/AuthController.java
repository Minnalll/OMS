package com.oms.auth.controller;

import com.oms.auth.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.oms.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidationResponse> validateToken(@RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(
                            ValidationResponse.builder()
                                    .valid(false)
                                    .message("Authorization header missing")
                                    .build()
                    );
        }

        String token = authorizationHeader.substring(7);
        return ResponseEntity.ok(
                authService.validateToken(token));
    }

}