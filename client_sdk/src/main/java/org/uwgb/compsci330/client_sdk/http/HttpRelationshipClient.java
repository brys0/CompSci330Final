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

public class HttpRelationshipClient extends HttpClient {
    public HttpRelationshipClient(Client client) {
        super(client);
    }

    public List<SafeRelationship> getRelationships() throws IOException {
        final Request request = this
                .createRequestWithAuthorization("/users/@me/relationships")
                .get()
                .build();

        return this.executeForList(request, SafeRelationship.class);
    }

    public SafeRelationship requestFriendship(String username) throws IOException {
        final Request request = this
                .createRequestWithAuthorization("/users/@me/relationships/" + username)
                .post(RequestBody.EMPTY)
                .build();

        return this.execute(request, SafeRelationship.class);
    }

    public void deleteFriendship(String username) throws IOException {
        final Request request = this
                .createRequestWithAuthorization("/users/@me/relationships/" + username)
                .delete()
                .build();

        this.execute(request);
    }
}
