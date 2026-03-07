package org.uwgb.compsci330.server.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.uwgb.compsci330.server.websocket.dto.out.OutboundEventType;

public class OutboundEvent {
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

    public long getSequence() {
        return sequence;
    }

    // INTERNAL API
    public void setSequence(long newSequence) {
            this.sequence = newSequence;
    }
}
