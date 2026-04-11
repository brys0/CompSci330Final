package org.uwgb.compsci330.client_sdk.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.common.websocket.model.in.authenticate.AuthenticateEvent;
import org.uwgb.compsci330.common.websocket.model.in.resume.RequestResumeEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.hello.HelloEvent;
import tools.jackson.core.type.TypeReference;

public class WebSocketEventHandler {
    private final Client client;
    private final WebSocketManager wsManager;
    private final EntityEventHandler entityEventHandler;
    private final HeartbeatManager heartbeatManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebSocketEventHandler(Client client, WebSocketManager wsManager) {
        this.client = client;
        this.wsManager = wsManager;
        this.entityEventHandler = new EntityEventHandler(client, wsManager.bus);
        this.heartbeatManager = new HeartbeatManager(wsManager);
    }

    public void handle(String rawMessage) {
        OutboundEvent<?> event = client.getConfig().getObjectMapper()
                .readValue(rawMessage, new TypeReference<OutboundEvent<?>>() {});

        logger.debug("Received event: {}", event.getType());

        wsManager.updateSeq(event.getSequence());

        switch (event.getType()) {
            case READY_TO_AUTHENTICATE -> sendAuthentication();
            case HELLO -> sendResumeIfApplicable(event);
            default -> entityEventHandler.handle(event);
        }
    }

    private void sendResumeIfApplicable(OutboundEvent<?> event) {
        final long lastSeq = wsManager.getLastSeq();
        // Events were received before disconnection
        if (lastSeq > 0) {
            logger.info("Resuming WebSocket from seq {}", lastSeq);
            wsManager.sendText(wsManager.serialize(new RequestResumeEvent(lastSeq)));
        }

        heartbeatManager.start((HelloEvent) event);
    }
    private void sendAuthentication() {
        logger.info("Authenticating");
        wsManager.sendText(wsManager.serialize(new AuthenticateEvent(
                client.getConfig().getToken()
        )));
    }

    public void onDisconnected() {
        heartbeatManager.stop();
    }
}