package org.uwgb.compsci330.client_sdk;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.http.HttpRelationshipClient;
import org.uwgb.compsci330.client_sdk.http.HttpUserClient;
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

    public Client(ClientConfig config) {
        this.config = config;
        this.httpRelationshipClient = new HttpRelationshipClient(this);
        this.httpUserClient = new HttpUserClient(this);
    }

    public void register(String username, String password) throws IOException {
        final String token = this.httpUserClient.registerWithUsernameAndPassword(new RegisterUserRequest(username, password));
        System.out.printf("Successfully registered: %s\n", username);

        this.config.setToken(token);
    }

    public void login(String username, String password) throws IOException {
        final String token = this.httpUserClient.loginWithUsernameAndPassword(new LoginUserRequest(username, password));
        System.out.printf("Successfully logged in: %s\n", username);

        this.config.setToken(token);
    }
}
