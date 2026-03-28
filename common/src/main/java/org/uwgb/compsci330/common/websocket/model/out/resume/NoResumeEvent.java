package org.uwgb.compsci330.common.websocket.model.out.resume;

import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

public class NoResumeEvent extends OutboundEvent<Void> {
    public NoResumeEvent() {
        super(OutboundEventType.NO_RESUME, null);
    }
}
