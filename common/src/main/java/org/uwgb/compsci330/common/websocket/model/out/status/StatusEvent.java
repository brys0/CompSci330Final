package org.uwgb.compsci330.common.websocket.model.out.status;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.model.response.user.UserStatus;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class StatusEvent extends OutboundEvent<StatusEventPayload> {
    public StatusEvent(String userId, UserStatus status) {
        super(OutboundEventType.STATUS, new StatusEventPayload(userId, status));
    }
}
