package com.oms.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.oms.auth.dto.RegisterRequest;
import com.oms.auth.dto.RegisterResponse;
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
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authService.register(request);

        return new ResponseEntity<>(response,
                HttpStatus.CREATED);

    }

}