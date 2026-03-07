package org.uwgb.compsci330.server.websocket.dto.in.resume;

import org.uwgb.compsci330.server.websocket.dto.InboundEvent;
import org.uwgb.compsci330.server.websocket.dto.in.InboundEventType;

public class RequestResumeEvent extends InboundEvent {
    public RequestResumeEvent(InboundEventType type, long sequence) {
        super(InboundEventType.REQUEST_RESUME, new RequestResumePayload(sequence));
    }
}
