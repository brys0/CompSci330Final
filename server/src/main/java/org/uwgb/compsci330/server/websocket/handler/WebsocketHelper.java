package org.uwgb.compsci330.server.websocket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.uwgb.compsci330.server.websocket.dto.InboundEvent;
import org.uwgb.compsci330.server.websocket.dto.OutboundEvent;
import org.uwgb.compsci330.server.websocket.raw.RawServerMessage;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public abstract class WebsocketHelper extends TextWebSocketHandler {
    public final ObjectMapper mapper;
    public WebsocketHelper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public void sendEvent(WebSocketSession session, OutboundEvent event) throws java.io.IOException {
        session.sendMessage(new RawServerMessage(mapper, event).getTextMessage());
    }

    abstract void onConnection(WebSocketSession session) throws Exception;
    abstract void onServerSocketClosed(WebSocketSession session, CloseStatus status);
    abstract void onClientSocketClosed(WebSocketSession session, CloseStatus status);

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        onClientSocketClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        onConnection(session);
    }

    // Convert the message to an inbound event, if the client doesn't respect the rules, we simply force close the websocket.
    public final <T extends InboundEvent> T messageToInboundEvent(WebSocketSession session, TextMessage message, Class<T> value) {
        try {
            return mapper.readValue(message.getPayload(), value);
        } catch (JacksonException e) {
            System.out.println("Jackson exception");
            onServerSocketClosed(session, CloseStatus.PROTOCOL_ERROR);
            return null;
        }
    }
}
