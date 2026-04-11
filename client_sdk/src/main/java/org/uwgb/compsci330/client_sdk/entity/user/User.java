package org.uwgb.compsci330.client_sdk.entity.user;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.IdentifiableEntity;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.model.response.user.SafeUser;
import org.uwgb.compsci330.common.model.response.user.UserStatus;

import java.io.IOException;
import java.util.Optional;

public class User implements IdentifiableEntity {
    @Getter
    private final Client client;
    private SafeUser data;

    public User(Client client, SafeUser user) {
        this.client = client;
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

    public void setStatus(UserStatus status) {
       this.data = new SafeUser(this.getId(), this.getUsername(), status);
    }

    public Optional<Relationship> getRelationship() {
        return client.getRelationships().getRelationshipByOtherUserId(this.getId());
    }

    public Relationship addRelationship() throws IOException {
        return client.getRelationships().createRelationship(this.getUsername());
    }
}
