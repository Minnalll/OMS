package com.oms.auth.service;

import com.oms.auth.dto.*;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    ValidationResponse validateToken(String token);

}