package org.uwgb.compsci330.client_sdk.entity.message;

import lombok.Getter;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.IdentifiableEntity;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.model.response.message.MessageType;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;

import java.time.LocalDateTime;

public class Message implements IdentifiableEntity {
    @Getter
    private final Client client;
    private final SafeMessage message;

    public Message(Client client, SafeMessage message) {
        this.client = client;
        this.message = message;
    }

    public String getId() {
        return this.message.getId();
    }

    public User getSender() {
        return this.client.getCache()
                .getOrCreate(
                        message.getSender().getId(),
                        User.class,
                        () -> new User(this.client, this.message.getSender())
                );
    }

    public String getContent() { return this.message.getContent(); }
    public LocalDateTime getCreatedAt() {
        return this.message.getCreatedAt();
    }
    
    public LocalDateTime getUpdatedAt() {
        return this.message.getUpdatedAt();
    }
    
    public MessageType getType() {
        return this.message.getType();
    }
}
