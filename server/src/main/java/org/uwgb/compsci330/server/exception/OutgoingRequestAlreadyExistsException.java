package org.uwgb.compsci330.server.exception;

public class OutgoingRequestAlreadyExistsException extends RuntimeException{
    public OutgoingRequestAlreadyExistsException(String otherUsername) {
        super(String.format("You can't create another outgoing request for \"%s\"", otherUsername));
    }
}
