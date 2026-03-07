package org.uwgb.compsci330.server.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.uwgb.compsci330.server.websocket.dto.in.InboundEventType;

public class InboundEvent {
    @JsonProperty("t")
    public final InboundEventType type;

    @JsonProperty("d")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public final Object payload;

    public InboundEvent(InboundEventType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

}
