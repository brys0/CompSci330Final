package org.uwgb.compsci330.common.websocket.model.in;

public enum InboundEventType {
    // Client sends authenticate message, with the token.
    AUTHENTICATE,
    REQUEST_RESUME
}
