package org.uwgb.compsci330.client_sdk.websocket;

import com.neovisionaries.ws.client.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.client_sdk.websocket.BusManager;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;

public class WebSocketManager implements Entity {
    @Getter
    private final Client client;
    private final WebSocketEventHandler eventHandler;
    private final ReconnectManager reconnectManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final BusManager bus = new BusManager();

    private Thread wsThread = null;
    private volatile WebSocket ws;
    private final String url;

    @Getter
    private volatile WebSocketState state = WebSocketState.CONNECTING;

    @Getter
    private volatile long lastSeq = 0;



    public WebSocketManager(Client client) {
        this.client = client;
        this.url = client.getConfig().getWsUrl() + "/ws";
        this.eventHandler = new WebSocketEventHandler(client, this);
        this.reconnectManager = new ReconnectManager(this);
    }

    public void updateSeq(long seq) {
        if (this.lastSeq > seq) return;

        this.lastSeq = seq;
    }

    public void connect() {
        state = WebSocketState.CONNECTING;
        logger.debug("Connecting to WebSocket {}", this.url);

        if (wsThread != null) {
            throw new IllegalStateException("WebSocket thread already exists");
        }

        wsThread = Thread.ofVirtual().start(() -> {
            try {
                ws = new WebSocketFactory()
                        .createSocket(this.url)
                        .addListener(new WebSocketAdapter() {
                            @Override
                            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                                state = WebSocketState.CONNECTED;
                                reconnectManager.reset();
                                logger.info("WebSocket connected");
                            }

                            @Override
                            public void onDisconnected(WebSocket websocket,
                                                       WebSocketFrame serverCloseFrame,
                                                       WebSocketFrame clientCloseFrame,
                                                       boolean closedByServer) {
                                eventHandler.onDisconnected();
                                if (state == WebSocketState.DISCONNECT) return;

                                reconnectManager.scheduleReconnect();
                            }

                            @Override
                            public void onTextMessage(WebSocket ws, String message) {
                                try {
                                    logger.debug("Received message {}", message);
                                    eventHandler.handle(message);
                                } catch (Exception e) {
                                    logger.error("Failed to handle message", e);
                                    disconnect();
                                }
                            }
                        })
                        .connect();
            } catch (Exception e) {
                // Couldn't connect, retry...
                if (e.getCause() instanceof ConnectException) {
                    logger.warn("Failed to connect");
                    reconnectManager.scheduleReconnect();
                    wsThread = null;
                    ws = null;
                    return;
                }
                throw new RuntimeException(e);
            }
        });
    }

    public void disconnect() {
        if (ws != null) ws.disconnect();

        ws = null;
        wsThread = null;
    }

    public void forceDisconnect() {
        state = WebSocketState.DISCONNECT;

        ws.disconnect();

        ws = null;
        wsThread = null;
    }

    public void sendText(String text) {
        if (ws != null) ws.sendText(text);
    }
    public String serialize(Object source) {
        try {
            final String payload = client.getConfig().getObjectMapper().writeValueAsString(source);
            logger.debug("Serializing: {}", payload);
            return payload;
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }

    enum WebSocketState {
        DISCONNECT,
        // Connecting to server.
        CONNECTING,
        // Connected but not yet authenticated with server.
        CONNECTED,
        // Connected and authenticated with server, receiving events
        READY
    }
}