package org.uwgb.compsci330.client_sdk.manager;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.client_sdk.entity.IdentifiableEntity;
import org.uwgb.compsci330.client_sdk.entity.message.Message;
import org.uwgb.compsci330.client_sdk.entity.relationship.Relationship;
import org.uwgb.compsci330.client_sdk.entity.user.User;
import org.uwgb.compsci330.common.model.request.message.CreateMessageRequest;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageManager implements IdentifiableEntity {
    @Getter
    private final String conversationId;

    @Getter
    private final Relationship relationship;
    @Getter
    final Client client;

    private final ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

    public MessageManager(Client client, Relationship relationship, String conversationId) {
        this.client = client;
        this.relationship = relationship;
        this.conversationId = conversationId;
    }


    public Message createMessage(String content) throws IOException {
        final SafeMessage created =
                client
                        .getHttpMessagesClient()
                        .createMessage(relationship.getUser().getId(), new CreateMessageRequest(content));
        final Message message = new Message(client, this, created);

        messages.put(message.getId(), message);

        return message;
    }

    public List<Message> getMessages() {
        return messages
                .values()
                .stream()
                .toList();
    }

    public List<Message> fetchMessages(int limit, @Nullable String afterId, @Nullable String beforeId) throws IOException {
        final List<SafeMessage> fetchedMessages =
                client
                        .getHttpMessagesClient()
                        .getMessages(this.relationship.getUser().getId(), limit, afterId, beforeId);
        final List<Message> realMessages = new ArrayList<>();

        for (var message : fetchedMessages) {
            final Message realMessage = new Message(client, this, message);
            messages.put(realMessage.getId(), realMessage);
            realMessages.add(realMessage);
        }

        return realMessages;
    }

    public void addMessage(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public String getId() {
        return this.conversationId;
    }
}
