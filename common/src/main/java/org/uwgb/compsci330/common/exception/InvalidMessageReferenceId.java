package org.uwgb.compsci330.common.exception;

public class InvalidMessageReferenceId extends RuntimeException {
    public InvalidMessageReferenceId(String message) {
        super(message);
    }

    public static InvalidMessageReferenceId create(String messageId) {
        return new InvalidMessageReferenceId(String.format("A message with id: \"%s\" doesn't exist.", messageId));
    }
}
