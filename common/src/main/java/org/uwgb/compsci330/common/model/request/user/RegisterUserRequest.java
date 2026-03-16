package org.uwgb.compsci330.common.model.request.user;


public class RegisterUserRequest {
    private String username;

    private String password;

    @SuppressWarnings("unused")
    public RegisterUserRequest() {}

    @SuppressWarnings("unused")
    public RegisterUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
