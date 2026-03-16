package org.uwgb.compsci330.common.websocket.model.out.message;

import org.uwgb.compsci330.common.websocket.model.event.OutboundEventWithIdentity;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

import java.util.Collection;

public class DeleteMessageEvent extends OutboundEventWithIdentity {
    public DeleteMessageEvent(String messageId, String... users) {
        super(
                OutboundEventType.MESSAGE,
                new MessageEventPayload(MessageEventType.MESSAGE_DELETE, messageId),
                users
        );
    }

    public DeleteMessageEvent(String messageId, Collection<String> users) {
        super(
                OutboundEventType.MESSAGE,
                new MessageEventPayload(MessageEventType.MESSAGE_DELETE, messageId),
                users
        );
    }
}