package org.uwgb.compsci330.server.websocket.dto.out;

import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;

public class NoResumeEvent extends OutboundEvent {
    public NoResumeEvent() {
        super(OutboundEventType.NO_RESUME, null);
    }
}
