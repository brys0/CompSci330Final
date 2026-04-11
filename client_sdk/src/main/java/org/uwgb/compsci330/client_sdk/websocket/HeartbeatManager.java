package org.uwgb.compsci330.client_sdk.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uwgb.compsci330.common.websocket.model.in.heartbeat.HeartbeatEvent;
import org.uwgb.compsci330.common.websocket.model.out.hello.HelloEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartbeatManager {
    private final WebSocketManager wsManager;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("ws-heartbeat");
        t.setDaemon(true);
        return t;
    });
    private ScheduledFuture<?> heartbeat = null;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HeartbeatManager(WebSocketManager wsManager) {
        this.wsManager = wsManager;
    }

    public void start(HelloEvent event) {
        long interval = event.getPayload().heartbeatInterval();
        logger.info("Starting heartbeat every {}ms", interval);

        heartbeat = executor.scheduleAtFixedRate(() -> {
            wsManager.sendText(
                    wsManager.serialize(new HeartbeatEvent())
            );
            logger.debug("Sent heartbeat");
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (heartbeat != null) {
            heartbeat.cancel(false);
            heartbeat = null;
            logger.debug("Heartbeat stopped");
        }
    }
}