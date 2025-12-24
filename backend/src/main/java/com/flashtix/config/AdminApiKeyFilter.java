package com.flashtix.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flashtix.common.dto.ApiResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminApiKeyFilter implements Filter {

    @Value("${admin.api.key}")
    private String adminApiKey;

    private final ObjectMapper objectMapper;

    public AdminApiKeyFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Only apply to admin endpoints
        if (httpRequest.getRequestURI().startsWith("/api/admin")) {
            String apiKey = httpRequest.getHeader("X-Admin-API-Key");

            if (apiKey == null || apiKey.isEmpty()) {
                sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED.value(),
                    "Admin API key is required");
                return;
            }

            if (!adminApiKey.equals(apiKey)) {
                sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN.value(),
                    "Invalid admin API key");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

