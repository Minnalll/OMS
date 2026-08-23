package com.oms.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oms.auth.dto.RegisterRequest;
import com.oms.auth.dto.RegisterResponse;
import com.oms.auth.model.User;
import com.oms.auth.exception.ResourceAlreadyExistsException;
import com.oms.auth.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository repository,
                           PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;

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

}