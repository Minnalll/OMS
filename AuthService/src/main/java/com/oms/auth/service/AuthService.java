package com.oms.auth.service;

import com.oms.auth.dto.RegisterRequest;
import com.oms.auth.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

}