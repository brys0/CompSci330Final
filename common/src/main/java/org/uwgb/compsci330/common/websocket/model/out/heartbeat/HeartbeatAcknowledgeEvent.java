package org.uwgb.compsci330.common.websocket.model.out.heartbeat;

import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

public class HeartbeatAcknowledgeEvent extends OutboundEvent<Void> {
    public HeartbeatAcknowledgeEvent() {
        super(OutboundEventType.HEARTBEAT_ACK, null);
    }
}
