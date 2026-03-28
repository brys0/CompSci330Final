package org.uwgb.compsci330.common.websocket.model.in.heartbeat;

import org.uwgb.compsci330.common.websocket.model.in.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;

public class HeartbeatEvent extends InboundEvent<Void> {
    public HeartbeatEvent() {
        super(InboundEventType.HEARTBEAT, null);
    }
}
