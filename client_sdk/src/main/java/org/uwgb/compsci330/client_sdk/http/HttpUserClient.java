package org.uwgb.compsci330.client_sdk.http;

import okhttp3.Request;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.SelfUser;
import org.uwgb.compsci330.common.model.request.user.LoginUserRequest;
import org.uwgb.compsci330.common.model.request.user.RegisterUserRequest;
import org.uwgb.compsci330.common.model.response.user.SafeUser;

import java.io.IOException;

public class HttpUserClient extends HttpClient{

    public HttpUserClient(Client client) {
        super(client);
    }

    public String registerWithUsernameAndPassword(RegisterUserRequest registerUserRequest) throws IOException {
        final Request request = this
                .createRequest("/users/register")
                .post(this.createBody(registerUserRequest))
                .build();

        return this.execute(request);
    }

    public String loginWithUsernameAndPassword(LoginUserRequest loginUserRequest) throws IOException {
        final Request request = this
                .createRequest("/users/login")
                .post(this.createBody(loginUserRequest))
                .build();

        return this.execute(request);
    }

    public SafeUser getMe() throws IOException {
        final Request request = this
                .createRequestWithAuthorization("/users/@me")
                .get()
                .build();

        return this.execute(request, SafeUser.class);
    }
}