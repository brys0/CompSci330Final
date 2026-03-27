package org.uwgb.compsci330.common.exception;

public class InvalidConversationException extends RuntimeException {
    public InvalidConversationException(String friendId) {
        super(friendId);
    }

    public static InvalidConversationException create(String friendId) {
        return new InvalidConversationException(String.format("A conversation with \"%s\" could not be found.", friendId));
    }
}
