package org.uwgb.compsci330.common.websocket.model.out;

import org.uwgb.compsci330.common.websocket.model.OutboundEvent;

public class NoResumeEvent extends OutboundEvent {
    public NoResumeEvent() {
        super(OutboundEventType.NO_RESUME, null);
    }
}
