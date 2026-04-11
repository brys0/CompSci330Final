package org.uwgb.compsci330.common.websocket.model.in.resume;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.websocket.model.in.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;

@NoArgsConstructor
public class RequestResumeEvent extends InboundEvent<RequestResumePayload> {
    public RequestResumeEvent(long sequence) {
        super(InboundEventType.REQUEST_RESUME, new RequestResumePayload(sequence));
    }
}
