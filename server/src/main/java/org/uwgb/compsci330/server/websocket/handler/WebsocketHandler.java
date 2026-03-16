package org.uwgb.compsci330.server.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.common.websocket.model.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.resume.RequestResumePayload;
import org.uwgb.compsci330.common.websocket.model.out.message.CreateMessageEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipEvent;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WebsocketHandler extends StatusWebsocket {

    public WebsocketHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @EventListener
    public void handleInternalRelationshipEvent(RelationshipEvent event) throws IOException {
        this.sendEvent(event);
    }

    @EventListener
    public void handleInternalCreateMessageEvent(CreateMessageEvent event) throws IOException {
        this.sendEvent(event);
    }

    @Override
    public void onServerSocketClosed(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClientMessage(WebSocketSession session, InboundEvent event) {
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
