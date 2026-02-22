package org.uwgb.compsci330.server.exception;

public class ExistingRelationshipException extends RuntimeException {
    public ExistingRelationshipException(String friendUsername) {
        super(String.format("You are already friends with \"%s\"", friendUsername));
    }
}
