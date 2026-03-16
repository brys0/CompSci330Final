package org.uwgb.compsci330.common.websocket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

public class OutboundEvent {
    // INTERNAL API
        @Getter
        @Setter
        @JsonProperty("sq")
        private long sequence = 0;

        @JsonProperty("t")
        final OutboundEventType type;

        @JsonProperty("d")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        final Object payload;

        @JsonProperty("at")
        final long timestamp;

        public OutboundEvent(OutboundEventType type, Object payload) {
               this.type = type;
               this.payload = payload;
               this.timestamp = System.currentTimeMillis();
        }

}
