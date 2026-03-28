package org.uwgb.compsci330.server.websocket.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.common.websocket.model.in.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.InboundEventType;
import org.uwgb.compsci330.common.websocket.model.out.heartbeat.HeartbeatAcknowledgeEvent;
import org.uwgb.compsci330.server.ServerConfiguration;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public abstract class HeartbeatWebsocket extends AuthenticatedWebsocket {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("(WS) heartbeat scheduler %d").build());
    final private int MAX_MISSED_HEARTBEATS = ServerConfiguration.MAX_MISSED_HEARTBEATS;

    private final Map<WebSocketSession, Long> lastHeartbeat = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Integer> missedHeartbeats = new ConcurrentHashMap<>();


    public HeartbeatWebsocket(ObjectMapper mapper) {
        super(mapper);
        this.checkHeartbeats();
    }

    protected void startHeartbeat(WebSocketSession session) {
        lastHeartbeat.put(session, System.currentTimeMillis());
        missedHeartbeats.put(session, 0);
    }

    protected void stopHeartbeat(WebSocketSession session) {
        lastHeartbeat.remove(session);
        missedHeartbeats.remove(session);
    }

    protected void receivedHeartbeat(WebSocketSession session) throws IOException {
        lastHeartbeat.put(session, System.currentTimeMillis());
        missedHeartbeats.put(session, 0);
        super.sendEventWithoutSequence(session, new HeartbeatAcknowledgeEvent());
    }


    private void checkHeartbeats() {
        executor.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            lastHeartbeat.forEach((session, last) -> {
                long elapsed = now - last;
                if (elapsed > ServerConfiguration.WEBSOCKET_HEARTBEAT_INTERVAL * 1.5) {
                    int missed = missedHeartbeats.merge(session, 1, Integer::sum);
                    System.out.println("Session " + session + " missed heartbeat #" + missed);

                    if (missed > MAX_MISSED_HEARTBEATS) {
                        if (session != null) {
                            onServerSocketClosed(session, CloseStatus.SESSION_NOT_RELIABLE);
                        }
                    }
                }
            });
        }, 0, ServerConfiguration.WEBSOCKET_HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }
}
