package org.uwgb.compsci330.server.websocket.dto.out.status;

import org.uwgb.compsci330.server.entity.user.UserStatus;
import org.uwgb.compsci330.server.websocket.dto.out.OutboundEventType;
import org.uwgb.compsci330.server.websocket.event.OutboundEventWithIdentity;

import java.util.Collection;

public class StatusEvent extends OutboundEventWithIdentity {
    public StatusEvent(String userId, UserStatus status, String... users) {
        super(OutboundEventType.STATUS, new StatusEventPayload(userId, status), users);
    }

    public StatusEvent(String userId, UserStatus status, Collection<String> users) {
        super(OutboundEventType.STATUS, new StatusEventPayload(userId, status), users);
    }
}
