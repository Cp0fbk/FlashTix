package com.flashtix.service;

import com.flashtix.dto.request.LoginRequest;
import com.flashtix.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
