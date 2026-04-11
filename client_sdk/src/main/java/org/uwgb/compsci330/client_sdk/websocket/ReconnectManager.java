package org.uwgb.compsci330.client_sdk.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReconnectManager {
    private static final long BASE_DELAY = 1000L; // 1s
    private static final long MAX_DELAY = 30000L; // 30s

    private final WebSocketManager wsManager;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("ws-reconnect");
        t.setDaemon(true);
        return t;
    });

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int attempts = 0;

    public ReconnectManager(WebSocketManager wsManager) {
        this.wsManager = wsManager;
    }

    public void scheduleReconnect() {
        long delay = Math.min(BASE_DELAY * (1L << attempts), MAX_DELAY);
        attempts++;

        logger.info("Reconnecting in {}ms (attempt {})", delay, attempts);

        executor.schedule(() -> {
            if (wsManager.getState() != WebSocketManager.WebSocketState.DISCONNECT) {
                wsManager.disconnect();
                wsManager.connect();
            }

        }, delay, TimeUnit.MILLISECONDS);
    }

    public void reset() {
        attempts = 0;
    }
}