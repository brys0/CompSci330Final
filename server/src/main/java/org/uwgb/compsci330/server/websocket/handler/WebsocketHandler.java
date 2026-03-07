package org.uwgb.compsci330.server.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.server.websocket.dto.InboundEvent;
import org.uwgb.compsci330.server.websocket.dto.in.resume.RequestResumePayload;
import org.uwgb.compsci330.server.websocket.dto.out.DummyEvent;
import org.uwgb.compsci330.server.websocket.dto.out.relationship.RelationshipEvent;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WebsocketHandler extends AuthenticatedWebsocket {
    private static final Logger log = LoggerFactory.getLogger(WebsocketHandler.class);

    public WebsocketHandler(ObjectMapper objectMapper) {
        super(objectMapper);

        // Test creating a ton of events.
        try {
            for (int i = 0; i < 10; i++) {
                super.sendEvent(new DummyEvent("4165d6d4-4c9f-4af8-b517-3817ae8f0516")); // Dummy test user
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventListener
    public void handleInternalRelationshipEvent(RelationshipEvent event) throws IOException {
        this.sendEvent(event);
    }

    @Override
    public void onServerSocketClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("A socket is closing forcefully.");
        try {
            session.close(status);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClientSocketClosed(CloseStatus status) {
        log.info("The socket was forcefully closed by the client.");
    }

    @Override
    public void onClientMessage(WebSocketSession session, InboundEvent event) {
        System.out.println("Got a client message!");

        switch(event.type) {
            case REQUEST_RESUME -> {
                final RequestResumePayload resumePayload = mapper.convertValue(event.payload, RequestResumePayload.class);
                try {
                    super.sendResume(resumePayload.seq(), session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
