package org.uwgb.compsci330.common.model.response.user;

import lombok.Getter;

// Classes annotated as safe hide information that should not be avaliable to the client.
public class SafeUser {
    @Getter
    private final String id;

    @Getter
    private final String username;

    @Getter
    private final UserStatus status;

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
