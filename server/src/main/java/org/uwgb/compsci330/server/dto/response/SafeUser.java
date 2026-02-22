package org.uwgb.compsci330.server.dto.response;

import org.uwgb.compsci330.server.entity.User;

// Classes annotated as safe hide information that should not be avaliable to the client.
public class SafeUser {
    private final String id;
    private final String username;
    private final int status;

    public SafeUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.status = user.getStatus();
    }

    public SafeUser(String id, String username, int status) {
        this.id = id;
        this.username = username;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getStatus() {
        return status;
    }
}
