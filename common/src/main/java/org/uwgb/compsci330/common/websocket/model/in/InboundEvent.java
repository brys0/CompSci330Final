package org.uwgb.compsci330.common.websocket.model.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.uwgb.compsci330.common.websocket.model.in.authenticate.AuthenticateEvent;
import org.uwgb.compsci330.common.websocket.model.in.heartbeat.HeartbeatEvent;
import org.uwgb.compsci330.common.websocket.model.in.resume.RequestResumeEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "t", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthenticateEvent.class, name = "AUTHENTICATE"),
        @JsonSubTypes.Type(value = RequestResumeEvent.class, name = "REQUEST_RESUME"),
        @JsonSubTypes.Type(value = HeartbeatEvent.class, name = "HEARTBEAT"),
})
public class InboundEvent<T> {
    @JsonProperty("t")
    public InboundEventType type;

    @JsonProperty("d")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T payload;

    public InboundEvent() {}

    public InboundEvent(InboundEventType type, T payload) {
        this.type = type;
        this.payload = payload;
    }

}
