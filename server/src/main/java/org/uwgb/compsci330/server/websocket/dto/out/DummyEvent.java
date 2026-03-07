package org.uwgb.compsci330.server.websocket.dto.out;

import org.uwgb.compsci330.server.websocket.event.OutboundEventWithIdentity;

public class DummyEvent extends OutboundEventWithIdentity {

    public DummyEvent(String... users) {
        super(OutboundEventType.DUMMY,null, users);
    }
}
