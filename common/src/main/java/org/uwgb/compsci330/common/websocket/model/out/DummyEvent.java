package org.uwgb.compsci330.common.websocket.model.out;

import org.uwgb.compsci330.common.websocket.model.event.OutboundEventWithIdentity;

public class DummyEvent extends OutboundEventWithIdentity {

    public DummyEvent(String... users) {
        super(OutboundEventType.DUMMY,null, users);
    }
}
