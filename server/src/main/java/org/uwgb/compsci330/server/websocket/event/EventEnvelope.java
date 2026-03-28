package org.uwgb.compsci330.server.websocket.event;

import lombok.Getter;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;

import java.util.Collection;
import java.util.List;

public class EventEnvelope<T extends OutboundEvent<?>> {
    @Getter
    private final T event;

    @Getter
    private final Collection<String> users;

    public EventEnvelope(T event, String ...users) {
        this.event = event;
        this.users = List.of(users);
    }

    public EventEnvelope(T event, Collection<String> users) {
        this.event = event;
        this.users = users;
    }
}
