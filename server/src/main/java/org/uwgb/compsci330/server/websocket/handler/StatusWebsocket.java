package org.uwgb.compsci330.server.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.uwgb.compsci330.common.model.response.user.UserStatus;
import org.uwgb.compsci330.common.websocket.model.out.status.StatusEvent;
import org.uwgb.compsci330.server.annotation.FragileSensitiveApi;
import org.uwgb.compsci330.server.service.RelationshipService;
import org.uwgb.compsci330.server.service.UserService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public abstract class StatusWebsocket extends AuthenticatedWebsocket {
    private static final Logger log = LoggerFactory.getLogger(StatusWebsocket.class);
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    private RelationshipService rs;

    @Autowired
    private UserService us;


    public StatusWebsocket(ObjectMapper objectMapper) {
        super(objectMapper);
    }
    @Override
    public void onClientSocketClosed(WebSocketSession session, CloseStatus status) {
        this.broadcastUserStatusEventToPeers(session, UserStatus.OFFLINE);
    }

    @Override
    public void onClientAuthenticated(WebSocketSession session) {
        this.broadcastUserStatusEventToPeers(session, UserStatus.ONLINE);
    }

    @FragileSensitiveApi
    private void broadcastUserStatusEventToPeers(WebSocketSession session, UserStatus status) {
        try {
            final String userId = (String) session.getAttributes().get("userId");
            if (userId == null) return;

            // Spin up virtual thread to fetch friends lazily
            executor.submit(() -> {
                final long startInvoke = System.currentTimeMillis();

                try {
                    us.setUserStatus(userId, status);

                    final Set<String> peerIds = rs.getRelationshipPeers(userId);
                    super.sendEventToUser(
                            new StatusEvent(
                                    userId,
                                    status,
                                    peerIds)
                    );

                    log.info("Broadcasted {} status ({}) for users {} (TTL: {}ms)", userId, status, peerIds, System.currentTimeMillis()-startInvoke);
                } catch (IOException e) {
                    log.error("Could not broadcast status({})", userId);
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            log.warn("Could not get user id for session {}", e.getMessage());
        }
    }
}
