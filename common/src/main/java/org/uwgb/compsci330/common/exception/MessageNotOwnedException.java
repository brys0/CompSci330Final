package org.uwgb.compsci330.common.exception;

public class MessageNotOwnedException extends RuntimeException {
    public MessageNotOwnedException(String messageId) {
        super(String.format("You must own %s to delete it", messageId));
    }
}
