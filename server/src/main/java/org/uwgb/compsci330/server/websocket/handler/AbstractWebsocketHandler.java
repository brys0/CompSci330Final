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

public abstract class AbstractWebsocketHandler extends TextWebSocketHandler {
    public final ObjectMapper mapper;
    public AbstractWebsocketHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public void sendEvent(WebSocketSession session, OutboundEvent event) throws java.io.IOException {
        session.sendMessage(new RawServerMessage(mapper, event).getTextMessage());
    }

    public void onServerSocketClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("The socket was closed forcefully, by the server, but this method was not overwritten correctly.");
        System.out.println("You are expected to call session.close(status) yourself.");
        // Method stub
    }

    public void onClientSocketClosed(CloseStatus status) {
        System.out.println("The socket was closed forcefully, by the client, but this method was not overwritten correctly.");
        // Method stub.
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        onClientSocketClosed(status);
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
