package com.flashtix.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpRequest {
    private String method;
    private String endpoint;
    private String payload;
    private String contentType;
}
