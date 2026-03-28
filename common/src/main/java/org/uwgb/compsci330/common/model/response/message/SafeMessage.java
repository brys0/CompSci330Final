package org.uwgb.compsci330.common.model.response.message;

import lombok.Getter;
import org.uwgb.compsci330.common.model.response.user.SafeUser;

import java.time.LocalDateTime;

public class SafeMessage {
    @Getter
    private String id;

    @Getter
    private SafeUser sender;

    @Getter
    private String content;

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime updatedAt;

    @Getter
    private MessageType type;

    public SafeMessage() {}

    public SafeMessage(
            String id,
            SafeUser sender,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            MessageType type
    ) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("(%s) %s (Sent by: %s)", this.id, this.content, this.sender.getUsername());
    }
}
