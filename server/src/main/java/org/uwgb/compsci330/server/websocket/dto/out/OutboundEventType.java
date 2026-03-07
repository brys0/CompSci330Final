package org.uwgb.compsci330.server.websocket.dto.out;

public enum OutboundEventType {
    // Server tells the client it's ready to receive authentication.
    READY_TO_AUTHENTICATE,
    // Server can't resume the last events for the client, either because the sequence was invalid, or the server restarted.
    NO_RESUME,
    HELLO,

    // Relationship related events
    RELATIONSHIP,
    DUMMY
}
