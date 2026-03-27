package org.uwgb.compsci330.common.exception;

public class OutgoingRequestAlreadyExistsException extends RuntimeException {
    public OutgoingRequestAlreadyExistsException(String message) {
        super(message);
    }

    public static OutgoingRequestAlreadyExistsException create(String otherUsername) {
        return new OutgoingRequestAlreadyExistsException(String.format("You can't create another outgoing request for \"%s\"", otherUsername));
    }
}
