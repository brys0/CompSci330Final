package org.uwgb.compsci330.client_sdk.manager;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;

import java.io.IOException;
import java.util.*;

public class RelationshipManager implements Entity {
    @Getter
    final Client client;

    final Set<String> relationships = new HashSet<>();
    public RelationshipManager(Client client) {
        this.client = client;
    }

    public Relationship createRelationship(String username) throws IOException {
        final SafeRelationship created = client
                .getHttpRelationshipClient()
                .requestFriendship(username);
        final Relationship relationship = new Relationship(client, created);

        client.getCache().put(
                relationship.getId(),
                Relationship.class,
                relationship
        );
        relationships.add(relationship.getId());

        return relationship;
    }


    public void deleteRelationship(String username, String relationshipId) throws IOException {
        client
                .getHttpRelationshipClient()
                .deleteFriendship(username);

        client
                .getCache()
                .invalidate(relationshipId);
        relationships.remove(relationshipId);
    }

    public Optional<Relationship> getRelationshipByOtherUserId(String id) {
        return client
                .getCache()
                .get(Relationship.createRelationshipKey(this.client, id), Relationship.class);
    }

    public List<Relationship> fetchRelationships() throws IOException {
        final List<Relationship> newRelationships = new ArrayList<>();

        for (SafeRelationship relationship : client.getHttpRelationshipClient().getRelationships()) {
            final Relationship entityRelationship = new Relationship(client, relationship);
            client.getCache().put(
                    entityRelationship.getConversation().getId(),
                    MessageManager.class,
                    entityRelationship.conversation
            );
            client
                    .getCache()
                    .put(entityRelationship.getId(), Relationship.class, entityRelationship);
            relationships.add(entityRelationship.getId());
            newRelationships.add(entityRelationship);
        }

        return newRelationships;
    }
}
