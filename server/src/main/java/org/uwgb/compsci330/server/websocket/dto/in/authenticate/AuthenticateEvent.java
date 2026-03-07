package org.uwgb.compsci330.server.websocket.dto.in.authenticate;

import org.uwgb.compsci330.server.websocket.dto.InboundEvent;
import org.uwgb.compsci330.server.websocket.dto.in.InboundEventType;

public class AuthenticateEvent extends InboundEvent {
    public AuthenticateEvent(String token) {
        super(InboundEventType.AUTHENTICATE,  new AuthenticateEventPayload(token));
    }
}

