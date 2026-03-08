package org.uwgb.compsci330.server.websocket.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;
import org.uwgb.compsci330.server.websocket.dto.out.OutboundEventType;

import java.util.Collection;
import java.util.List;

public class OutboundEventWithIdentity extends OutboundEvent {
    @JsonIgnore
    private final Collection<String> users;

    public OutboundEventWithIdentity(OutboundEventType type, Object payload, String ...users) {
        super(type, payload);
        this.users = List.of(users);
    }

    public OutboundEventWithIdentity(OutboundEventType type, Object payload, Collection<String> users) {
        super(type, payload);
        this.users = users;
    }

    @JsonIgnore
    public Collection<String> getUsers() {
        return this.users;
    }
}
