package org.uwgb.compsci330.server.websocket.raw;

import org.springframework.web.socket.TextMessage;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import tools.jackson.databind.ObjectMapper;

public class RawServerMessage {
    private final TextMessage message;
    public RawServerMessage(ObjectMapper mapper, OutboundEvent event) {
        this.message = new TextMessage(mapper.writeValueAsString(event));
    }

    public TextMessage getTextMessage() {
        return this.message;
    }
}
