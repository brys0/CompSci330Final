package org.uwgb.compsci330.server.websocket.handler;

import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.server.Configuration;
import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;
import org.uwgb.compsci330.server.websocket.dto.out.NoResumeEvent;
import org.uwgb.compsci330.server.websocket.event.OutboundEventWithIdentity;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractSequencedWebsocket extends AbstractWebsocketHandler {
    private final AtomicInteger currentSq = new AtomicInteger(0);
    private Map<String, Deque<OutboundEvent>> events = new ConcurrentHashMap<>();

    public AbstractSequencedWebsocket(ObjectMapper mapper) {
        super(mapper);
    }

    public final void sendEvent(OutboundEventWithIdentity event) throws java.io.IOException {
        final int seq = currentSq.getAndIncrement();
        event.setSequence(seq);

        // Add event to queue
        for (String userId : event.getUsers()) {
            Deque<OutboundEvent> queue = events.computeIfAbsent(userId,
                    k -> new LinkedBlockingDeque<>(Configuration.MAX_RESUME_BUFFER));

            synchronized (queue) {
                if (queue.size() >= Configuration.MAX_RESUME_BUFFER) {
                    queue.removeFirst(); // Evict oldest
                }
                queue.addLast(event);
            }
        }

        sendEventToUser(event);
    }

    public final void sendEventWithoutSequence(WebSocketSession session, OutboundEvent event) throws java.io.IOException {
        super.sendEvent(session, event);
    }


    public void sendEventToUser(OutboundEventWithIdentity event) throws IOException {
        // stub
        System.out.println("This method should be overridden to send the events to the proper user.");
    }

    public final void sendResume(long lastSequence, WebSocketSession session) throws IOException {
        String userId = (String) session.getAttributes().get("userId");
        Deque<OutboundEvent> queue = events.get(userId);

        if (queue == null || queue.isEmpty()) {
            return;
        }

        if (queue.peekFirst().getSequence() > lastSequence + 1) {
            this.sendEventWithoutSequence(session, new NoResumeEvent());
            return;
        }

        for (OutboundEvent event : queue) {
            if (event.getSequence() > lastSequence) {
                this.sendEventWithoutSequence(session, event);
            }
        }
    }
}
