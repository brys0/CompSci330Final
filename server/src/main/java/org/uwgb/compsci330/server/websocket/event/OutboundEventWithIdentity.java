package org.uwgb.compsci330.server.websocket.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;
import org.uwgb.compsci330.server.websocket.dto.out.OutboundEventType;

import java.util.List;

public class OutboundEventWithIdentity extends OutboundEvent {
    @JsonIgnore
    private final List<String> users;

    public OutboundEventWithIdentity(OutboundEventType type, Object payload, String ...users) {
        super(type, payload);
        this.users = List.of(users);
    }

    public OutboundEventWithIdentity(OutboundEventType type, Object payload, List<String> users) {
        super(type, payload);
        this.users = users;
    }

    @JsonIgnore
    public List<String> getUsers() {
        return this.users;
    }
}
