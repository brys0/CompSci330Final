package org.uwgb.compsci330.server.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.common.exception.*;
import org.uwgb.compsci330.common.model.response.message.MessageType;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.server.ServerConfiguration;
import org.uwgb.compsci330.server.annotation.FragileSensitiveApi;
import org.uwgb.compsci330.server.annotation.SensitiveApi;
import org.uwgb.compsci330.server.dto.request.CreateMessageRequest;
import org.uwgb.compsci330.server.entity.conversation.Conversation;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.mapper.MessageMapper;
import org.uwgb.compsci330.server.repository.ConversationRepository;
import org.uwgb.compsci330.server.repository.MessageRepository;
import org.uwgb.compsci330.server.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @SensitiveApi
    @FragileSensitiveApi
    public Conversation createConversation(Set<User> users) {
        Conversation conversation = new Conversation(users);
        conversation = conversationRepository.save(conversation);

        messageService.createMessage(null, conversation, "Say hi! You're now friends :)", MessageType.SYSTEM);

        return conversation;
    }

    public List<SafeMessage> getMessages(
            String userId,
            String friendId,
            @Nullable String afterId,
            @Nullable String beforeId,
            int limit
    ) {
        if (limit <= 0) throw new MessageLimitZeroOrLessException();
        if (limit > ServerConfiguration.MAX_MESSAGE_FETCH_LIMIT) throw new MessageLimitExceededException();
        if (afterId != null && beforeId != null) throw new InvalidConversationFetchOptions();

        final Conversation conversation = conversationRepository
                .findDirectConversation(userId, friendId)
                .orElseThrow(() -> new InvalidConversationException(friendId));

        if (afterId != null && !messageRepository.existsById(afterId)) {
            throw new InvalidMessageReferenceId(afterId);
        }

        if (beforeId != null && !messageRepository.existsById(beforeId)) {
            throw new InvalidMessageReferenceId(beforeId);
        }

        final PageRequest page = PageRequest.of(0, limit);

        if (afterId != null) {
            return messageRepository
                    .findMessagesAfterMessageId(conversation, afterId, page)
                    .stream()
                    .map(MessageMapper::toSafe)
                    .toList();
        }

        if (beforeId != null) {
            return messageRepository
                    .findMessagesBeforeMessageId(conversation, beforeId, page)
                    .stream()
                    .map(MessageMapper::toSafe)
                    .toList();
        }

        return messageRepository
                .findLatestMessagesFromConversation(conversation, page)
                .stream()
                .map(MessageMapper::toSafe)
                .toList();
    }

    public SafeMessage createMessage(
            String userId,
            String friendId,
            CreateMessageRequest createMessage
    ) {
        final Conversation conversation = conversationRepository
                .findDirectConversation(userId, friendId)
                .orElseThrow(() -> new InvalidConversationException(friendId));

        final User sender = userRepository
                .findUserById(userId)
                .orElseThrow(UnauthorizedException::new);

        if (createMessage.getContent().length() > ServerConfiguration.MAX_MESSAGE_LENGTH) throw new MaxMessageLengthExceededException();
        if (createMessage.getContent().isBlank()) throw new InvalidMessageLengthException();

        return messageService
                .createMessage(
                        sender,
                        conversation,
                        createMessage.getContent(),
                        MessageType.USER
                );
    }

    @FragileSensitiveApi
    @Transactional
    public void deleteMessage(
            String userId,
            String friendId,
            String messageId
    ) {
        final Conversation conversation = conversationRepository
                .findDirectConversation(userId, friendId)
                .orElseThrow(() -> new InvalidConversationException(friendId));

        final SafeMessage message = messageRepository
                .findMessageById(messageId)
                .map(MessageMapper::toSafe)
                .orElseThrow(() -> new InvalidMessageReferenceId(messageId));

        if (message.getSender() != null) {
            if (!Objects.equals(message.getSender().getId(), userId))
                throw new MessageNotOwnedException(messageId);
        } else { // System user, deleted user?
            throw new MessageNotOwnedException(messageId);
        }


        messageService.deleteMessage(conversation, messageId);
    }
}
