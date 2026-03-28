package org.uwgb.compsci330.common.websocket.model.out.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class MessageDeletedEvent extends OutboundEvent<MessageDeletedEvent.MessageDeleteEventPayload> {
    public MessageDeletedEvent(String messageId) {
        super(
                OutboundEventType.MESSAGE_DELETED,
                new MessageDeleteEventPayload(messageId)
        );
    }


    record MessageDeleteEventPayload(
            @JsonInclude(value = JsonInclude.Include.NON_NULL)
            String messageId
    ) {}
}

