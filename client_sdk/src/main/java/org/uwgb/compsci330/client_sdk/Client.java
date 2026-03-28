package org.uwgb.compsci330.client_sdk;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.uwgb.compsci330.client_sdk.entity.SelfUser;
import org.uwgb.compsci330.client_sdk.http.HttpRelationshipClient;
import org.uwgb.compsci330.client_sdk.http.HttpUserClient;
import org.uwgb.compsci330.client_sdk.websocket.WebSocketManager;
import org.uwgb.compsci330.common.model.request.user.LoginUserRequest;
import org.uwgb.compsci330.common.model.request.user.RegisterUserRequest;

import java.io.IOException;

public class Client {
    @Getter
    private final ClientConfig config;

    @Getter
    private final HttpRelationshipClient httpRelationshipClient;

    @Getter
    private final HttpUserClient httpUserClient;

    @Getter
    @Nullable
    private SelfUser self;

    @Getter
    private final WebSocketManager ws;

    public Client(ClientConfig config) {
        this.config = config;
        this.httpRelationshipClient = new HttpRelationshipClient(this);
        this.httpUserClient = new HttpUserClient(this);
        this.ws = new WebSocketManager(this);

        if (config.getToken() != null) {
            try {
                this.fetchAndSetSelf();
            } catch (IOException e) {
                System.out.printf("There was an issue when trying to fetch user on client initialization: %s\n", e);
            }
        }
    }

    public SelfUser register(String username, String password) throws IOException {
        final String token = this.httpUserClient.registerWithUsernameAndPassword(new RegisterUserRequest(username, password));
        System.out.printf("Successfully registered: %s\n", username);

        this.config.setToken(token);
        this.fetchAndSetSelf();

        return this.self;
    }

    public SelfUser login(String username, String password) throws IOException {
        final String token = this.httpUserClient.loginWithUsernameAndPassword(new LoginUserRequest(username, password));
        System.out.printf("Successfully logged in: %s\n", username);

        this.config.setToken(token);
        this.fetchAndSetSelf();

        return this.self;
    }

    private void fetchAndSetSelf() throws IOException {
        final SelfUser user = new SelfUser(this, this.httpUserClient.getMe());
        this.self = user;

        System.out.printf("Got self user: %s (%s) status: %s\n", user.getUsername(), user.getId(), user.getStatus());
    }
}
