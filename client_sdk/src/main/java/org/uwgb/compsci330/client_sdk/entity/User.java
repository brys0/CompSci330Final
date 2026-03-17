package org.uwgb.compsci330.client_sdk.entity;

import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.model.response.user.SafeUser;
import org.uwgb.compsci330.common.model.response.user.UserStatus;

import java.io.IOException;

public class User extends Entity {
    private final SafeUser data;

    protected User(Client client, SafeUser user) {
        super(client);
        this.data = user;
    }

    public String getId() {
        return this.data.getId();
    }
    public String getUsername() {
        return this.data.getUsername();
    }

    public UserStatus getStatus() {
        return this.data.getStatus();
    }

    public SafeRelationship addRelationship() throws IOException {
           return client.getHttpRelationshipClient().requestFriendship(this.data.getUsername());
    }
    public void removeRelationship() throws IOException {
        client.getHttpRelationshipClient().deleteFriendship(this.data.getUsername());
    }
}
