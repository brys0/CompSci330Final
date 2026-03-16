package org.uwgb.compsci330.common.websocket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;

public class InboundEvent {
    @JsonProperty("t")
    public InboundEventType type;

    @JsonProperty("d")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object payload;

    public InboundEvent() {}

    public InboundEvent(InboundEventType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

}
