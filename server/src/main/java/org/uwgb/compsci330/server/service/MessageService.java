package org.uwgb.compsci330.server.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.common.model.response.message.MessageType;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.common.websocket.model.out.message.CreateMessageEvent;
import org.uwgb.compsci330.common.websocket.model.out.message.DeleteMessageEvent;
import org.uwgb.compsci330.server.annotation.SensitiveApi;
import org.uwgb.compsci330.server.entity.conversation.Conversation;
import org.uwgb.compsci330.server.entity.message.Message;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.mapper.MessageMapper;
import org.uwgb.compsci330.server.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    public SafeMessage createMessage(@Nullable User sender, Conversation conversation, String content, MessageType type) {
        Message message = new Message(sender, conversation, content, type);
        messageRepository.save(message);

        final SafeMessage safeMessage = MessageMapper.toSafe(message);

        publisher.publishEvent(
                new CreateMessageEvent(
                        safeMessage,
                        conversation.getParticipants().stream()
                                .map(User::getId)
                                .toList()
                )
        );

        return safeMessage;
    }

    @Transactional
    @SensitiveApi
    public void deleteMessage(Conversation conversation, String messageId) {
        messageRepository.deleteById(messageId);

        publisher.publishEvent(
                new DeleteMessageEvent(
                        messageId,
                        conversation.getParticipants()
                                .stream()
                                .map(User::getId)
                                .toList()
                )
        );
    }
}
