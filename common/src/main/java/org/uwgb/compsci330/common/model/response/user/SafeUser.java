package org.uwgb.compsci330.common.model.response.user;

import lombok.Getter;

// Classes annotated as safe hide information that should not be avaliable to the client.
public class SafeUser {
    @Getter
    private String id;

    @Getter
    private String username;

    @Getter
    private UserStatus status;

    public SafeUser() {}

    public SafeUser(
            String id,
            String username,
            UserStatus status
    ) {
        this.id = id;
        this.username = username;
        this.status = status;
    }
}
