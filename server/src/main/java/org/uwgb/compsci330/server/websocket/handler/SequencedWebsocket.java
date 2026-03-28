package org.uwgb.compsci330.server.websocket.handler;

import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.resume.NoResumeEvent;
import org.uwgb.compsci330.server.ServerConfiguration;
import org.uwgb.compsci330.server.websocket.event.EventEnvelope;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequencedWebsocket extends WebsocketHelper {
    private final AtomicInteger currentSq = new AtomicInteger(0);
    private final Map<String, Deque<OutboundEvent<?>>> events = new ConcurrentHashMap<>();

    public SequencedWebsocket(ObjectMapper mapper) {
        super(mapper);
    }

    public final void sendEvent(EventEnvelope<OutboundEvent<?>> envelope) throws java.io.IOException {
        final OutboundEvent<?> event = envelope.getEvent();
        final int seq = currentSq.getAndIncrement();
        event.setSequence(seq);

        // Add event to queue
        for (String userId : envelope.getUsers()) {
            Deque<OutboundEvent<?>> queue = events.computeIfAbsent(userId,
                    k -> new LinkedBlockingDeque<>(ServerConfiguration.MAX_RESUME_BUFFER));

            synchronized (queue) {
                if (queue.size() >= ServerConfiguration.MAX_RESUME_BUFFER) {
                    queue.removeFirst(); // Evict oldest
                }
                queue.addLast(event);
            }
        }

        sendEventToUser(envelope);
    }

    public final void sendEventWithoutSequence(WebSocketSession session, OutboundEvent<?> event) throws java.io.IOException {
        super.sendEvent(session, event);
    }

    abstract void sendEventToUser(EventEnvelope<OutboundEvent<?>> event) throws IOException;

    public final void sendResume(long lastSequence, WebSocketSession session) throws IOException {
        String userId = (String) session.getAttributes().get("userId");
        Deque<OutboundEvent<?>> queue = events.get(userId);

        if (queue == null || queue.isEmpty()) {
            return;
        }

        if (queue.peekFirst().getSequence() > lastSequence + 1) {
            this.sendEventWithoutSequence(session, new NoResumeEvent());
            return;
        }

        for (OutboundEvent<?> event : queue) {
            if (event.getSequence() > lastSequence) {
                this.sendEventWithoutSequence(session, event);
            }
        }
    }
}
