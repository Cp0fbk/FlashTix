package com.flashtix.service;

import com.flashtix.dto.LoginRequest;
import com.flashtix.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
