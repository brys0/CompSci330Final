package org.uwgb.compsci330.common.websocket.model.out.message.delete;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class MessageDeletedEvent extends OutboundEvent<MessageDeleteEventPayload> {
    public MessageDeletedEvent(String messageId) {
        super(
                OutboundEventType.MESSAGE_DELETED,
                new MessageDeleteEventPayload(messageId)
        );
    }



}

