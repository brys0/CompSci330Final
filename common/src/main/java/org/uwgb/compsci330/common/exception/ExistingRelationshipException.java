package org.uwgb.compsci330.common.exception;

public class ExistingRelationshipException extends RuntimeException {
    public ExistingRelationshipException(String message) {
        super(message);
    }

    public static ExistingRelationshipException create(String friendUsername) {
        return new ExistingRelationshipException(String.format("You are already friends with \"%s\"", friendUsername));
    }
}
