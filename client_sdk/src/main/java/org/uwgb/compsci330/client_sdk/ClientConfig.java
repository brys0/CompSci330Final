package org.uwgb.compsci330.client_sdk;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.TimeUnit;

public class ClientConfig {
    @Getter
    private final String baseUrl;

    @Getter
    private final String wsUrl;

    @Getter
    private final OkHttpClient httpClient;

    @Getter
    private final JsonMapper objectMapper;

    @Nullable
    @Getter
    @Setter
    private String token;

    public static final int MAX_CACHE_SIZE = 1000;

    public ClientConfig(String baseUrl, String wsUrl) {
        this.baseUrl = baseUrl;
        this.wsUrl = wsUrl;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    final Request request = chain.request().newBuilder()
                            .header("Content-Type", "application/json")
                            .build();

                    return chain.proceed(request);
                })
                .build();
        this.objectMapper = new JsonMapper();
    }


}
