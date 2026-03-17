package org.uwgb.compsci330.client_sdk.http;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.common.exception.*;
import org.uwgb.compsci330.common.model.response.ErrorResponse;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;

import java.io.IOException;
import java.util.List;

public class HttpRelationshipClient extends Entity {
    private final ClientConfig config;
    public HttpRelationshipClient(Client client) {
        super(client);
        this.config = client.getConfig();
    }

    public List<SafeRelationship> getRelationships() throws IOException {
        Request request = new Request.Builder()
                .url(config.getBaseUrl() + "/users/@me/relationships")
                .header("Authorization", config.getToken())
                .get()
                .build();

        try (final Response response = config.getHttpClient().newCall(request).execute()) {
            final String body = response.body().string();

            if (!response.isSuccessful()) handleRelationshipResponseErrors(body, "");

            return config
                    .getObjectMapper()
                    .readValue(
                            body,
                            config
                                    .getObjectMapper()
                                    .getTypeFactory()
                                    .constructCollectionType(List.class, SafeRelationship.class)
                    );
        }
    }

    public SafeRelationship requestFriendship(String username) throws IOException {
        Request request = new Request.Builder()
                .url(config.getBaseUrl() + "/users/@me/relationships/" + username)
                .header("Authorization", config.getToken())
                .post(RequestBody.EMPTY)
                .build();

        try (Response response = config.getHttpClient().newCall(request).execute()) {
            final String body = response.body().string();

            if (!response.isSuccessful()) handleRelationshipResponseErrors(body, username);

            return config
                    .getObjectMapper()
                    .readValue(body, SafeRelationship.class);
        }
    }

    public void deleteFriendship(String username) throws IOException {
        Request request = new Request.Builder()
                .url(config.getBaseUrl() + "/users/@me/relationships/" + username)
                .header("Authorization", config.getToken())
                .delete()
                .build();

        try (Response response = config.getHttpClient().newCall(request).execute()) {
            final String body = response.body().string();

            if (!response.isSuccessful()) handleRelationshipResponseErrors(body, username);
        }
    }

    private void handleRelationshipResponseErrors(String body, String username) {
        final ErrorResponse errorResponse = config.getObjectMapper().readValue(body, ErrorResponse.class);
        final String errorType = errorResponse.getType();

        switch(errorType) {
            case "InvalidFriendRequestException" -> throw new InvalidFriendRequestException(username);
            case "ExistingRelationshipException" -> throw new ExistingRelationshipException(username);
            case "OutgoingRequestAlreadyExistsException" -> throw new OutgoingRequestAlreadyExistsException(username);
            case "SelfFriendException" -> throw new SelfFriendException();
            case "RelationshipDoesNotExistException" -> throw new RelationshipDoesNotExistException(username);
            case "ReservedUsernameException" -> throw new ReservedUsernameException();
        }
    }
}
