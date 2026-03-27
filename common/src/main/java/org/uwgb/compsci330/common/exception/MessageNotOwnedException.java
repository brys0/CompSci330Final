package org.uwgb.compsci330.common.exception;

public class MessageNotOwnedException extends RuntimeException {
    public MessageNotOwnedException(String message) {
        super(message);
    }

    public static MessageNotOwnedException create(String messageId) {
        return new MessageNotOwnedException(String.format("You must own %s to delete it", messageId));
    }
}
