package org.uwgb.compsci330.server.websocket.handler;

import io.jsonwebtoken.Claims;
import org.jspecify.annotations.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.server.security.JwtUtil;
import org.uwgb.compsci330.server.websocket.dto.InboundEvent;
import org.uwgb.compsci330.server.websocket.dto.in.AuthenticationRequiredEvent;
import org.uwgb.compsci330.server.websocket.dto.in.authenticate.AuthenticateEventPayload;
import org.uwgb.compsci330.server.websocket.dto.out.HelloEvent;
import org.uwgb.compsci330.server.websocket.event.OutboundEventWithIdentity;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AuthenticatedWebsocket extends SequencedWebsocket {
    public AuthenticatedWebsocket(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    private final Map<String, Boolean> authenticatedSessions = new ConcurrentHashMap<>();
    private final Map<String, Set<WebSocketSession>> authenticatedUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        this.authenticatedSessions.put(session.getId(), false);
        super.sendEventWithoutSequence(session, new AuthenticationRequiredEvent());
    }

    abstract void onClientMessage(WebSocketSession session, InboundEvent event);

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        deleteUserSession(session);
        onClientSocketClosed(status);
    }

    @Override
    public final void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            InboundEvent genericEvent = super.messageToInboundEvent(session, message, InboundEvent.class);

            if (genericEvent == null) {
                return;
            }

            switch (genericEvent.type) {
                case AUTHENTICATE -> {
                    // Already authenticated, close the socket.
                    if (authenticatedSessions.get(session.getId())) {
                        onServerSocketClosed(session, CloseStatus.PROTOCOL_ERROR);
                        return;
                    }

                    final AuthenticateEventPayload auth = mapper.convertValue(genericEvent.payload, AuthenticateEventPayload.class);

                    try {
                        final Claims claims = JwtUtil.validateToken(auth.token());
                        final String userId = claims.get("userId", String.class);

                        authenticatedSessions.put(session.getId(), true);
                        session.getAttributes().put("userId", userId);
                        this.addUserSession(userId, session);

                        super.sendEventWithoutSequence(session, new HelloEvent());
                    } catch (Exception e) {
                        // Authentication probably failed.
                        System.out.println("A session sent an invalid authentication token.");
                        onServerSocketClosed(session, CloseStatus.POLICY_VIOLATION);
                    }
                }

                default -> {
                    // All other messages
                    if (!authenticatedSessions.get(session.getId())) {
                        System.out.println("A session tried sending a message without authenticating first. Dropping the connection..");
                        onServerSocketClosed(session, CloseStatus.POLICY_VIOLATION);
                        return;
                    }

                    this.onClientMessage(session, genericEvent);
                }
            }
        } catch (Exception e) {
            // Failed to parse message
            System.out.println("Could not parse message.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEventToUser(OutboundEventWithIdentity event) throws IOException {
        for (String userId : event.getUsers()) {
            Set<WebSocketSession> sessions = this.authenticatedUsers.get(userId);
            if (sessions != null) {
                for (WebSocketSession s : sessions) {
                    if (s.isOpen()) {
                        // Use the super method so we don't accidentally
                        // increment the sequence or re-buffer it.
                        super.sendEventWithoutSequence(s, event);
                    }
                }
            }
        }
    }

    private void addUserSession(String userId, WebSocketSession session) {
        this.authenticatedUsers.computeIfAbsent(userId, k -> new HashSet<>()).add(session);
    }

    private void deleteUserSession(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");

        if (userId != null) {
            Set<WebSocketSession> userSessions = this.authenticatedUsers.get(userId);
            if (userSessions != null) {
                userSessions.remove(session);

                if (userSessions.isEmpty()) {
                    this.authenticatedUsers.remove(userId);
                }
            }
        }
    }
}
