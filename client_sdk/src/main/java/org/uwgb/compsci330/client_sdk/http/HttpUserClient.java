package org.uwgb.compsci330.client_sdk.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.common.exception.*;
import org.uwgb.compsci330.common.model.request.user.LoginUserRequest;
import org.uwgb.compsci330.common.model.request.user.RegisterUserRequest;
import org.uwgb.compsci330.common.model.response.ErrorResponse;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;

import java.io.IOException;

public class HttpUserClient  extends Entity {
    private final ClientConfig config;

    public HttpUserClient(Client client) {
        super(client);
        this.config = client.getConfig();
    }

    public String registerWithUsernameAndPassword(RegisterUserRequest registerUserRequest) throws IOException {
        Request request = new Request.Builder()
                .url(config.getBaseUrl() + "/users/register")
                .post(RequestBody.create(config.getObjectMapper().writeValueAsBytes(registerUserRequest)))
                .build();

        try (Response response = config.getHttpClient().newCall(request).execute()) {
            final String body = response.body().string();

            if (!response.isSuccessful()) handleUserResponseErrors(body, registerUserRequest.getUsername());

            return body;
        }
    }
    public String loginWithUsernameAndPassword(LoginUserRequest loginUserRequest) throws IOException {
        Request request = new Request.Builder()
                .url(config.getBaseUrl() + "/users/login")
                .post(RequestBody.create(config.getObjectMapper().writeValueAsBytes(loginUserRequest)))
                .build();

        try (Response response = config.getHttpClient().newCall(request).execute()) {
            final String body = response.body().string();

            if (!response.isSuccessful()) handleUserResponseErrors(body, loginUserRequest.getUsername());

            return body;
        }
    }

    public void handleUserResponseErrors(String body, String username) throws JsonProcessingException {
        final ErrorResponse errorResponse = config.getObjectMapper().readValue(body, ErrorResponse.class);
        final String errorType = errorResponse.getType();

        switch (errorType) {
            case "UsernameTooShortException" -> throw new UsernameTooShortException(username);
            case "UsernameTooLongException" -> throw new UsernameTooLongException(username);
            case "UserAlreadyExistsException" -> throw new UserAlreadyExistsException(username);
            case "PasswordTooShortException" -> throw new PasswordTooShortException();
            case "UsernameOrPasswordIncorrectException" -> throw new UsernameOrPasswordIncorrectException();
            case "ReservedUsernameException" -> throw new ReservedUsernameException();
        }
    }
}