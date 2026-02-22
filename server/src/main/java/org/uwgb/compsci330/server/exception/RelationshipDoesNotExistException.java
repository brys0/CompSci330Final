package org.uwgb.compsci330.server.exception;

public class RelationshipDoesNotExistException extends RuntimeException {
    public RelationshipDoesNotExistException(String userId) {
        super(String.format("You do not currently have an outgoing, incoming, or active relationship with \"%s\"", userId));
    }
}
