package org.uwgb.compsci330.common.websocket.model.in;

import org.uwgb.compsci330.common.websocket.model.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

public class AuthenticationRequiredEvent extends OutboundEvent {
    public AuthenticationRequiredEvent() {
        // We can assume sq zero, cuz it should be the first event the server sends to the client
        super(OutboundEventType.READY_TO_AUTHENTICATE, null);
    }
}
