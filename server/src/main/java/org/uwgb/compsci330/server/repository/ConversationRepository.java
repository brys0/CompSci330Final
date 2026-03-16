package org.uwgb.compsci330.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.uwgb.compsci330.server.entity.conversation.Conversation;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
    @Query("""
    SELECT c FROM conversations c
    WHERE SIZE(c.participants) = 2
    AND EXISTS (SELECT p FROM c.participants p WHERE p.id = :userId)
    AND EXISTS (SELECT p FROM c.participants p WHERE p.id = :friendId)
""")
    Optional<Conversation> findDirectConversation(
            String userId,
            String friendId
    );
}
