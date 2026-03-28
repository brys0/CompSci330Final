package org.uwgb.compsci330.common.websocket.model.in.authenticate;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.websocket.model.in.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;

@NoArgsConstructor
public class AuthenticateEvent extends InboundEvent<AuthenticateEventPayload> {
    public AuthenticateEvent(String token) {
        super(InboundEventType.AUTHENTICATE, new AuthenticateEventPayload(token));
    }
}

