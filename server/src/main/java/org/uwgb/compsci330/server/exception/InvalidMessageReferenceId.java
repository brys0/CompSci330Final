package org.uwgb.compsci330.server.exception;

public class InvalidMessageReferenceId extends RuntimeException {
    public InvalidMessageReferenceId(String messageId) {
        super(String.format("A message with id: \"%s\" doesn't exist.", messageId));
    }
}
