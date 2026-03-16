package org.uwgb.compsci330.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.server.entity.conversation.Conversation;
import org.uwgb.compsci330.server.entity.message.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("SELECT m FROM messages m WHERE m.id = :messageId")
    Optional<Message> findMessageById(String messageId);

    @Query("SELECT m FROM messages m WHERE m.conversation = :conversation ORDER BY m.createdAt DESC")
    List<Message> findLatestMessagesFromConversation(Conversation conversation, Pageable pageable);

    @Query("SELECT m FROM messages m WHERE m.conversation = :conversation AND m.createdAt > (SELECT m2.createdAt FROM messages m2 WHERE m2.id = :messageId) ORDER BY m.createdAt ASC")
    List<Message> findMessagesAfterMessageId(
            Conversation conversation,
            String messageId,
            Pageable pageable
    );

    @Query("SELECT m FROM messages m WHERE m.conversation = :conversation AND m.createdAt < (SELECT m2.createdAt FROM messages m2 WHERE m2.id = :messageId) ORDER BY m.createdAt DESC")
    List<Message> findMessagesBeforeMessageId(
            Conversation conversation,
            String messageId,
            Pageable pageable
    );
}
