package org.uwgb.compsci330.common.exception;

public class RelationshipDoesNotExistException extends RuntimeException {
    public RelationshipDoesNotExistException(String message) {
        super(message);
    }

    public static RelationshipDoesNotExistException create(String username) {
        return new RelationshipDoesNotExistException(String.format("You do not currently have an outgoing, incoming, or active relationship with \"%s\"", username));
    }
}
