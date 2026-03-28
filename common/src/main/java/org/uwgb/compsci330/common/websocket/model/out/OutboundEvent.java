package org.uwgb.compsci330.common.websocket.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.uwgb.compsci330.common.websocket.model.out.authentication.AuthenticationRequiredEvent;
import org.uwgb.compsci330.common.websocket.model.out.heartbeat.HeartbeatAcknowledgeEvent;
import org.uwgb.compsci330.common.websocket.model.out.hello.HelloEvent;
import org.uwgb.compsci330.common.websocket.model.out.message.MessageCreatedEvent;
import org.uwgb.compsci330.common.websocket.model.out.message.MessageDeletedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipCreatedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipDeletedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipPendingEvent;
import org.uwgb.compsci330.common.websocket.model.out.resume.NoResumeEvent;
import org.uwgb.compsci330.common.websocket.model.out.status.StatusEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "t", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthenticationRequiredEvent.class, name = "READY_TO_AUTHENTICATE"),
        @JsonSubTypes.Type(value = NoResumeEvent.class, name = "NO_RESUME"),
        @JsonSubTypes.Type(value = HelloEvent.class, name = "HELLO"),
        @JsonSubTypes.Type(value = HeartbeatAcknowledgeEvent.class, name = "HEARTBEAT_ACK"),
        @JsonSubTypes.Type(value = RelationshipPendingEvent.class, name = "RELATIONSHIP_PENDING"),
        @JsonSubTypes.Type(value = RelationshipCreatedEvent.class, name = "RELATIONSHIP_CREATED"),
        @JsonSubTypes.Type(value = RelationshipDeletedEvent.class, name = "RELATIONSHIP_DELETED"),
        @JsonSubTypes.Type(value = StatusEvent.class, name = "STATUS"),
        @JsonSubTypes.Type(value = MessageCreatedEvent.class, name = "MESSAGE_CREATED"),
        @JsonSubTypes.Type(value = MessageDeletedEvent.class, name = "MESSAGE_DELETED")
})
public class OutboundEvent<T> {
    // INTERNAL API
        @Getter
        @Setter
        @JsonProperty("sq")
        private long sequence = 0;

        @JsonProperty("t")
        @Getter
        private OutboundEventType type;

        @JsonProperty("d")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Getter
        private T payload;

        @JsonProperty("at")
        @Getter
        private long timestamp;

        public OutboundEvent() {}

        public OutboundEvent(OutboundEventType type, T payload) {
               this.type = type;
               this.payload = payload;
               this.timestamp = System.currentTimeMillis();
        }

}
