package org.uwgb.compsci330.common.exception;

public class RelationshipDoesNotExistException extends RuntimeException {
    public RelationshipDoesNotExistException(String username) {
        super(String.format("You do not currently have an outgoing, incoming, or active relationship with \"%s\"", username));
    }
}
