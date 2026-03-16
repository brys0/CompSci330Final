package org.uwgb.compsci330.server.entity.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.uwgb.compsci330.common.model.response.message.MessageType;
import org.uwgb.compsci330.server.entity.conversation.Conversation;
import org.uwgb.compsci330.server.entity.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "messages")
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private String id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @Getter
    private Conversation conversation;

    @Getter
    @Setter
    private String content;

    @ManyToOne
    @Getter
    private User sender;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Getter
    private MessageType type; // SYSTEM, USER

    public Message() {}

    public Message(User sender, Conversation conversation, String content, MessageType type) {
        this.sender = sender;
        this.conversation = conversation;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.type = type;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
