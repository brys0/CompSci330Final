package org.uwgb.compsci330.server.websocket.dto.in;

public enum InboundEventType {
    // Client sends authenticate message, with the token.
    AUTHENTICATE,
    REQUEST_RESUME
}
