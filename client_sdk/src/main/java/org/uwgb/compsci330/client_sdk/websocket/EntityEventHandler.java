package org.uwgb.compsci330.client_sdk.websocket;

import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.message.Message;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.client_sdk.manager.MessageManager;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.message.MessageCreatedEvent;
import org.uwgb.compsci330.common.websocket.model.out.message.delete.MessageDeletedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipCreatedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipDeletedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipPendingEvent;
import org.uwgb.compsci330.common.websocket.model.out.status.StatusEvent;

import java.util.Optional;

public class EntityEventHandler {
    private final Client client;
    private final BusManager bus;

    public EntityEventHandler(Client client, BusManager bus) {
        this.client = client;
        this.bus = bus;
    }

    void handle(OutboundEvent<?> event) {
        switch (event.getType()) {
            case RELATIONSHIP_PENDING -> {
                RelationshipPendingEvent e = (RelationshipPendingEvent) event;
                Relationship relationship = new Relationship(client, e.getPayload());
                client
                        .getCache()
                        .put(
                                relationship.getId(),
                                Relationship.class,
                                relationship
                        );

                bus.dispatch(event.getType(), relationship);
            }
            case RELATIONSHIP_CREATED -> {
                RelationshipCreatedEvent e = (RelationshipCreatedEvent) event;
                Relationship relationship = new Relationship(client, e.getPayload());

                client.getCache().put(relationship.getId(), Relationship.class, relationship);
                // cache MessageManager by conversation ID for message routing
                client.getCache().put(
                        e.getPayload().getConversationId(),
                        MessageManager.class,
                        relationship.getConversation()
                );

                bus.dispatch(event.getType(), relationship);
            }
            case RELATIONSHIP_DELETED -> {
                RelationshipDeletedEvent e = (RelationshipDeletedEvent) event;
                final Relationship rel = new Relationship(client, e.getPayload());
                final String relationshipKey = Relationship.createRelationshipKey(client, e.getPayload());

                client
                        .getCache()
                        .invalidate(relationshipKey);

                bus.dispatch(event.getType(), rel.getUser().getId()); // just the id, entity is gone
            }
            case MESSAGE_CREATED -> {
                MessageCreatedEvent e = (MessageCreatedEvent) event;
                SafeMessage payload = e.getPayload();

                client.getCache().get(payload.getConversationId(), MessageManager.class)
                        .ifPresent(manager -> {
                            Message message = new Message(client, manager, payload);
                            manager.addMessage(message);
                            client.getCache().put(payload.getId(), Message.class, message);
                            bus.dispatch(event.getType(), message);
                        });
            }
            case MESSAGE_DELETED -> {
                MessageDeletedEvent e = (MessageDeletedEvent) event;
                client
                        .getCache()
                        .invalidate(e.getPayload().messageId());

                bus.dispatch(event.getType(), e.getPayload().messageId());
            }
            case STATUS -> {
                StatusEvent e = (StatusEvent) event;
                final Optional<User> user = client.getCache().get(e.getPayload().userId(), User.class);
                if (user.isEmpty()) return;
                final User realUser = user.get();

                realUser.setStatus(e.getPayload().status());

                // update cached user status if user is in cache
                client
                        .getCache()
                        .put(
                               realUser.getId(),
                                User.class,
                                realUser
                        );

                bus.dispatch(event.getType(), e.getPayload());
            }
        }
    }
}
