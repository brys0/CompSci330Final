package org.uwgb.compsci330.client_sdk.websocket;

import org.uwgb.compsci330.client_sdk.event.EventListener;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BusManager {
    private final Map<OutboundEventType, List<EventListener<OutboundEvent<?>>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends OutboundEvent<?>> void on(OutboundEventType type, EventListener<T> listener) {
        listeners.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>())
                .add((EventListener<OutboundEvent<?>>) listener);
    }

    public void off(OutboundEventType type, EventListener<?> listener) {
        listeners.getOrDefault(type, List.of()).remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    protected void dispatch(OutboundEvent<?> event) {
        List<EventListener<OutboundEvent<?>>> handlers = listeners.get(event.getType());
        if (handlers != null) {
            handlers.forEach(h -> h.onEvent(event));
        }
    }
}
