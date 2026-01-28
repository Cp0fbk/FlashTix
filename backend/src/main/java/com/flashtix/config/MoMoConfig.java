package com.flashtix.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConfigurationProperties(prefix = "momo")
@Data
public class MoMoConfig {
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String createUrl;
    private String redirectUrl;
    private String ipnUrl;

    @PostConstruct
    public void validate() {
        if (!StringUtils.hasText(partnerCode)) {
            throw new IllegalStateException("MoMo configuration error: partnerCode is required");
        }
        if (!StringUtils.hasText(accessKey)) {
            throw new IllegalStateException("MoMo configuration error: accessKey is required");
        }
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalStateException("MoMo configuration error: secretKey is required");
        }
        if (!StringUtils.hasText(endpoint)) {
            throw new IllegalStateException("MoMo configuration error: endpoint is required");
        }
        if (!StringUtils.hasText(createUrl)) {
            throw new IllegalStateException("MoMo configuration error: createUrl is required");
        }
        if (!StringUtils.hasText(redirectUrl)) {
            throw new IllegalStateException("MoMo configuration error: redirectUrl is required");
        }
        if (!StringUtils.hasText(ipnUrl)) {
            throw new IllegalStateException("MoMo configuration error: ipnUrl is required");
        }
    }
}
