package org.uwgb.compsci330.common.websocket.model.in.authenticate;

import org.uwgb.compsci330.common.websocket.model.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;

public class AuthenticateEvent extends InboundEvent {
    public AuthenticateEvent(String token) {
        super(InboundEventType.AUTHENTICATE,  new AuthenticateEventPayload(token));
    }
}

