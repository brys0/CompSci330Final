package org.uwgb.compsci330.server.websocket.handler;

import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.common.websocket.model.in.InboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.in.resume.RequestResumePayload;
import org.uwgb.compsci330.server.websocket.event.EventEnvelope;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WebsocketHandler extends StatusWebsocket {

    public WebsocketHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @EventListener
    public void handleInternalEvents(EventEnvelope<OutboundEvent<?>> envelope) throws IOException {
        this.sendEvent(envelope);
    }

    @Override
    public void onServerSocketClosed(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
            stopHeartbeat(session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void onClientMessage(WebSocketSession session, InboundEvent<?> event) {
        switch(event.type) {
            case REQUEST_RESUME -> {
                final RequestResumePayload resumePayload = mapper.convertValue(event.payload, RequestResumePayload.class);
                try {
                    super.sendResume(resumePayload.seq(), session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            case HEARTBEAT -> receivedHeartbeat(session);
        }
    }
}
