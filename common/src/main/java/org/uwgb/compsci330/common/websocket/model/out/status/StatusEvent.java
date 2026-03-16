package org.uwgb.compsci330.common.websocket.model.out.status;

import org.uwgb.compsci330.common.model.response.user.UserStatus;
import org.uwgb.compsci330.common.websocket.model.event.OutboundEventWithIdentity;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

import java.util.Collection;

public class StatusEvent extends OutboundEventWithIdentity {
    public StatusEvent(String userId, UserStatus status, String... users) {
        super(OutboundEventType.STATUS, new StatusEventPayload(userId, status), users);
    }

    public StatusEvent(String userId, UserStatus status, Collection<String> users) {
        super(OutboundEventType.STATUS, new StatusEventPayload(userId, status), users);
    }
}
