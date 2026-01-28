package com.flashtix.common.dto;

import lombok.Builder;
import lombok.Data;
import okhttp3.Headers;

@Data
@Builder
public class HttpResponse {

    int status;
    String data;
    Headers headers;

    public String toString() {
        return "HttpStatusCode:: " + this.status + ", ResponseBody:: " + this.data;
    }

}
