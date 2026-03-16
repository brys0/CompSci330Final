package org.uwgb.compsci330.common.websocket.model.out.message;

import org.uwgb.compsci330.common.model.response.message.SafeMessage;
import org.uwgb.compsci330.common.websocket.model.event.OutboundEventWithIdentity;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

import java.util.Collection;

public class CreateMessageEvent extends OutboundEventWithIdentity {
    public CreateMessageEvent(SafeMessage message, String... users) {
        super(
                OutboundEventType.MESSAGE,
                new MessageEventPayload(MessageEventType.MESSAGE_CREATE, message),
                users
        );
    }

    public CreateMessageEvent(SafeMessage message, Collection<String> users) {
        super(
                OutboundEventType.MESSAGE,
                new MessageEventPayload(MessageEventType.MESSAGE_CREATE, message),
                users
        );
    }
}
