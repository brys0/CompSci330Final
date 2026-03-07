package org.uwgb.compsci330.server.websocket.dto.in;

import org.uwgb.compsci330.server.websocket.dto.out.OutboundEventType;
import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;

public class AuthenticationRequiredEvent extends OutboundEvent {
    public AuthenticationRequiredEvent() {
        // We can assume sq zero, cuz it should be the first event the server sends to the client
        super(OutboundEventType.READY_TO_AUTHENTICATE, null);
    }
}
