package org.uwgb.compsci330.common.websocket.model.out;

import org.uwgb.compsci330.common.websocket.model.OutboundEvent;

public class HelloEvent extends OutboundEvent {
    public HelloEvent() {
        super(OutboundEventType.HELLO, null);
    }
}
