package org.uwgb.compsci330.client_sdk.http;

import lombok.Getter;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.common.exception.*;
import org.uwgb.compsci330.common.model.response.ErrorResponse;

import java.io.IOException;
import java.util.List;

public class HttpClient implements Entity {
    @Getter
    private final Client client;
    protected final ClientConfig config;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HttpClient(Client client) {
        this.client = client;
        this.config = client.getConfig();
    }

    protected Request.Builder createRequestWithAuthorization(String path) {
        logger.debug("Creating Authorized request at: {}, with token: {}", path, config.getToken());
        return this.createRequest(path).header("Authorization", config.getToken());
    }

    protected Request.Builder createRequest(String path) {
        return new Request.Builder().url(config.getBaseUrl() + path);
    }

    protected <T> RequestBody createBody(T type) {
        return RequestBody.create(this.config.getObjectMapper().writeValueAsBytes(type));
    }

    protected String execute(Request request) throws IOException {
        try (Response response = config.getHttpClient().newCall(request).execute()) {
            final String body = response.body().string();
            if (!response.isSuccessful()) handleResponseError(body);
            return body;
        }
    }

    protected  <T> T execute(Request request, Class<T> type) throws IOException {
        final String req = execute(request);

        System.out.println(req);
        return config.getObjectMapper().readValue(execute(request), type);
    }

    protected  <T> List<T> executeForList(Request request, Class<T> type) throws IOException {
        return config.getObjectMapper().readValue(
                execute(request),
                config.getObjectMapper().getTypeFactory().constructCollectionType(List.class, type)
        );
    }

    protected void handleResponseError(String body) {
        final ErrorResponse errorResponse = config.getObjectMapper().readValue(body, ErrorResponse.class);
        final String errorType = errorResponse.getType();
        final String message = errorResponse.getMessage();

        switch (errorType) {
            case "UsernameTooShortException" -> throw new UsernameTooShortException(message);
            case "UsernameTooLongException" -> throw new UsernameTooLongException(message);
            case "UserAlreadyExistsException" -> throw new UserAlreadyExistsException(message);
            case "PasswordTooShortException" -> throw new PasswordTooShortException();
            case "UsernameOrPasswordIncorrectException" -> throw new UsernameOrPasswordIncorrectException();
            case "InvalidFriendRequestException" -> throw new InvalidFriendRequestException(message);
            case "ExistingRelationshipException" -> throw new ExistingRelationshipException(message);
            case "OutgoingRequestAlreadyExistsException" -> throw new OutgoingRequestAlreadyExistsException(message);
            case "SelfFriendException" -> throw new SelfFriendException();
            case "RelationshipDoesNotExistException" -> throw new RelationshipDoesNotExistException(message);
            case "ReservedUsernameException" -> throw new ReservedUsernameException();
        }
    }
}
