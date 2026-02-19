package org.uwgb.compsci330.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterUserRequest {
    @NotBlank
    @Size(min = 2, max = 8)
    private String username;
    @NotBlank
    @Size(min = 7)
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
