package org.uwgb.compsci330.common.websocket.model.out.hello;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class HelloEvent extends OutboundEvent<HelloPayload> {
    public HelloEvent(long heartbeatInterval) {
        super(OutboundEventType.HELLO, new HelloPayload(heartbeatInterval));
    }
}

