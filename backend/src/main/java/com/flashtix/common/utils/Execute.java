package com.flashtix.common.utils;

import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

import com.flashtix.common.dto.HttpRequest;
import com.flashtix.common.dto.HttpResponse;

public class Execute {

    OkHttpClient client = new OkHttpClient();

    public HttpResponse sendToMoMo(String endpoint, String payload) {

        try {

            HttpRequest httpRequest = HttpRequest.builder()
                    .method("POST")
                    .endpoint(endpoint)
                    .payload(payload)
                    .contentType("application/json")
                    .build();

            Request request = createRequest(httpRequest);

            LogUtils.debug("[HttpPostToMoMo] Endpoint:: " + httpRequest.getEndpoint() + ", RequestBody:: "
                    + httpRequest.getPayload());

            Response result = client.newCall(request).execute();
            HttpResponse response = HttpResponse.builder()
                    .status(result.code())
                    .data(result.body().string())
                    .headers(result.headers())
                    .build();

            LogUtils.info("[HttpResponseFromMoMo] " + response.toString());

            return response;
        } catch (Exception e) {
            LogUtils.error("[ExecuteSendToMoMo] " + e);
        }

        return null;
    }

    public static Request createRequest(HttpRequest request) {
        RequestBody body = RequestBody.create(request.getPayload(), MediaType.get(request.getContentType()));
        return new Request.Builder()
                .method(request.getMethod(), body)
                .url(request.getEndpoint())
                .build();
    }

    public String getBodyAsString(Request request) throws IOException {
        Buffer buffer = new Buffer();
        RequestBody body = request.body();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }
}
