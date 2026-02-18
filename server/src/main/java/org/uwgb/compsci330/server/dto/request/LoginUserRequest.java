package org.uwgb.compsci330.server.dto.request;

public class LoginUserRequest {
    private String username;
    private String password;

    public LoginUserRequest() {}

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
