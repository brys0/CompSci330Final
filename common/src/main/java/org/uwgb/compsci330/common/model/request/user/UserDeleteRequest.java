package org.uwgb.compsci330.common.model.request.user;

public class UserDeleteRequest {
    private String password;

    public UserDeleteRequest() {}
    public UserDeleteRequest(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
