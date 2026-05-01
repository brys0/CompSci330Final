package org.uwgb.compsci330.client_sdk.websocket;

import org.uwgb.compsci330.client_sdk.event.EventListener;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BusManager {
    private final Map<OutboundEventType, List<EventListener<Object>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> EventListener<T> on(OutboundEventType type, EventListener<T> listener) {
        listeners.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>())
                .add((EventListener<Object>) listener);

        return listener;
    }

    public void off(OutboundEventType type, EventListener<?> listener) {
        listeners.getOrDefault(type, List.of()).remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    public void dispatch(OutboundEventType type, Object entity) {
        List<EventListener<Object>> handlers = listeners.get(type);
        if (handlers != null) {
            handlers.forEach(h -> h.onEvent(entity));
        }
    }
}