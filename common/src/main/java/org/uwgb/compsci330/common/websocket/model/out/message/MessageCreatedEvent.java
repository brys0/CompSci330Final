package org.uwgb.compsci330.common.websocket.model.out.message;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class MessageCreatedEvent extends OutboundEvent<SafeMessage> {
    public MessageCreatedEvent(SafeMessage message) {
        super(
                OutboundEventType.MESSAGE_CREATED,
                message
        );
    }
}
