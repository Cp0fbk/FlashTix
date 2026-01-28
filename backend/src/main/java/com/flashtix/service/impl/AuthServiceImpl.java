package com.flashtix.service.impl;

import com.flashtix.common.utils.JwtUtil;
import com.flashtix.dto.request.LoginRequest;
import com.flashtix.dto.response.LoginResponse;
import com.flashtix.entity.User;
import com.flashtix.repository.UserRepository;
import com.flashtix.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            // Get user details
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            // Return response
            return LoginResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
