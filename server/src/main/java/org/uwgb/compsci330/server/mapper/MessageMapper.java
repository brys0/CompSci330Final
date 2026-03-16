package org.uwgb.compsci330.server.mapper;

import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.server.entity.message.Message;
import org.uwgb.compsci330.server.entity.user.User;

public class MessageMapper {
    public static SafeMessage toSafe(Message message) {
        final User sender = message.getSender();
        return new SafeMessage(
                message.getId(),
                sender != null ? UserMapper.toSafe(message.getSender()) : null,
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getType()
        );
    }
}
