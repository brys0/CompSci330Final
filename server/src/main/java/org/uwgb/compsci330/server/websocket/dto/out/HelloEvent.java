package org.uwgb.compsci330.server.websocket.dto.out;

import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;

public class HelloEvent extends OutboundEvent {
    public HelloEvent() {
        super(OutboundEventType.HELLO, null);
    }
}
