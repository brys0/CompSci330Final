package org.uwgb.compsci330.common.websocket.model.in.resume;

import org.uwgb.compsci330.common.websocket.model.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;

public class RequestResumeEvent extends InboundEvent {
    public RequestResumeEvent(InboundEventType type, long sequence) {
        super(InboundEventType.REQUEST_RESUME, new RequestResumePayload(sequence));
    }
}
