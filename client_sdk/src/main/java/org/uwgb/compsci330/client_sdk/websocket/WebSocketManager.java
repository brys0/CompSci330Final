package org.uwgb.compsci330.client_sdk.websocket;

import com.neovisionaries.ws.client.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.common.websocket.model.in.heartbeat.HeartbeatEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.authenticate.AuthenticateEvent;
import org.uwgb.compsci330.common.websocket.model.out.hello.HelloEvent;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WebSocketManager extends Entity {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> heartbeat = null;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final BusManager bus = new BusManager();
    private Thread wsThread = null;
    private volatile WebSocket ws;
    private final String url;
    private volatile long lastSeq = 0;

    @Getter
    private volatile WebSocketState state = WebSocketState.CONNECTING;

    public WebSocketManager(Client client) {
        super(client);

        this.url = client.getConfig().getWsUrl() + "/ws";
    }

    public void connect() {
        logger.debug("Connecting to WebSocket {}", this.url);

        if (wsThread != null) {
            logger.warn("WebSocket thread already exists!");
            throw new IllegalStateException("You cannot create a new WebSocket connection with an existing thread");
        }

        logger.debug("Spawning virtual thread for WebSocket events");
        wsThread = Thread.ofVirtual().start(() -> {
            logger.debug("Started virtual thread");

            try {
                ws = new WebSocketFactory()
                        .createSocket(this.url)
                        .addListener(new WebSocketAdapter() {
                            @Override
                            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                                state = WebSocketState.CONNECTED;

                                logger.info("WebSocket connected");
                            }

                            @Override
                            public void onDisconnected(WebSocket websocket,
                                                       WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                                       boolean closedByServer) throws Exception
                            {
                                heartbeat.cancel(false);
                                heartbeat = null;
                                logger.warn("Disconnected from WebSocket: {} (server) {} (client) upstream: {}", serverCloseFrame.getCloseReason(), clientCloseFrame.getCloseReason(), closedByServer);
                            }

                            @Override
                            public void onTextMessage(WebSocket ws, String message) {
                                logger.debug("Received message: {}", message);
                                try {
                                    handleMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    disconnect();
                                }
                            }
                        })
                        .connect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void disconnect() {
        if (wsThread == null) {
            logger.error("Cannot disconnect from WebSocket because no thread exists");
            return;
        }
        if (ws != null) ws.disconnect();

        logger.warn("WebSocket disconnected");
        wsThread = null;
        ws = null;
    }

    private void handleMessage(String rawMessage) {
        OutboundEvent<?> event = deserializeMessage(rawMessage);
        lastSeq = event.getSequence();

        switch (event.getType()) {
            case READY_TO_AUTHENTICATE -> sendAuthentication();
            case HELLO -> handleHello((HelloEvent) event);
        }

        logger.debug("Dispatching event: {}", event.getType());
        this.bus.dispatch(event);
    }

    private void sendAuthentication() {
        ws.sendText(
                serializeMessage(new AuthenticateEvent(this.client.getConfig().getToken()))
        );
    }

    private void handleHello(HelloEvent event) {
        this.state = WebSocketState.READY;
        logger.info("WebSocket said hello! Spawning heartbeat scheduled thread...");

        heartbeat = executor.scheduleAtFixedRate(() -> {
            ws.sendText(serializeMessage(new HeartbeatEvent()));
            logger.debug("Sent heartbeat event");

        }, 0, event.getPayload().heartbeatInterval(), TimeUnit.MILLISECONDS);
    }

    private <T> String serializeMessage(T source) {
        final String payload = this.client.getConfig().getObjectMapper().writeValueAsString(source);
        logger.debug("Sending message: {}", payload);

        return payload;
    }

    private OutboundEvent<?> deserializeMessage(String rawMessage) {
        return this.client.getConfig().getObjectMapper()
                .readValue(rawMessage, new TypeReference<OutboundEvent<?>>() {});
    }

    private enum WebSocketState {
        // Connecting to server.
        CONNECTING,
        // Connected but not yet authenticated with server.
        CONNECTED,
        // Connected and authenticated with server, receiving events
        READY
    }
}
