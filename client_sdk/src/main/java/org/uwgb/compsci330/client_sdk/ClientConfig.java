package org.uwgb.compsci330.client_sdk;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

public class ClientConfig {
    @Getter
    private final String baseUrl;

    @Getter
    private final OkHttpClient httpClient;

    @Getter
    private final ObjectMapper objectMapper;
    @Getter
    @Setter
    private String token;

    public ClientConfig(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }


}
