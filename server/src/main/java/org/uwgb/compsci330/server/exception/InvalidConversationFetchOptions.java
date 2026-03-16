package org.uwgb.compsci330.server.exception;

public class InvalidConversationFetchOptions extends RuntimeException {
    public InvalidConversationFetchOptions() {
        super("You can't have ?after and ?before query params");
    }
}
