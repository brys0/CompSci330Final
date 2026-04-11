package org.uwgb.compsci330.client_sdk.entity.relationship;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.IdentifiableEntity;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.model.response.relationship.RelationshipStatus;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.model.response.user.SafeUser;

import java.io.IOException;
import java.util.Objects;

public class Relationship implements IdentifiableEntity {
    @Getter
    private final Client client;
    private final SafeRelationship relationship;

    public Relationship(Client client, SafeRelationship relationship) {
        this.client = client;
        this.relationship = relationship;
    }

    @Override
    public String getId() {
        return createRelationshipKey(client, this.relationship);
    }

    public User getRequester() {
        return this
                .client
                .getCache()
                .getOrCreate(
                        this.relationship.getRequester().getId(),
                        User.class,
                        () -> new User(this.client, this.relationship.getRequester())
        );
    }

    public User getRequestee() {
        return this
                .client
                .getCache()
                .getOrCreate(
                        this.relationship.getRequestee().getId(),
                        User.class,
                        () -> new User(this.client, this.relationship.getRequestee())
                );
    }

    public RelationshipStatus getStatus() {
        return this.relationship.getStatus();
    }


    public void remove() throws IOException {
        final SafeUser user = getEffectiveUser(this.client, this.relationship);
        this.client.getRelationships().deleteRelationship(user.getUsername(), this.getId());
    }

    public static SafeUser getEffectiveUser(Client client, SafeRelationship relationship) {
        return Objects.equals(relationship.getRequestee().getId(), client.getSelf().getId())
                ? relationship.getRequester()
                : relationship.getRequestee();
    }

    public static String createRelationshipKey(String id, String otherUserId) {
        return id+otherUserId;
    }

    public static String createRelationshipKey(Client client, String otherUserId) {
        return Relationship.createRelationshipKey(client.getSelf().getId(), otherUserId);
    }

    public static String createRelationshipKey(Client client, SafeRelationship relationship) {
        assert client.getSelf() != null;

        return createRelationshipKey(client, getEffectiveUser(client, relationship).getId());
    }
}
