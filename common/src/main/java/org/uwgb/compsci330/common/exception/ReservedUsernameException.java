package org.uwgb.compsci330.common.exception;

public class ReservedUsernameException extends RuntimeException {
    public ReservedUsernameException() {
        super("That username is reserved for System use only.");
    }
}
