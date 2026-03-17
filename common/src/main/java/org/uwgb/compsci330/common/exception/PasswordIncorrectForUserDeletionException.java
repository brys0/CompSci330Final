package org.uwgb.compsci330.common.exception;

public class PasswordIncorrectForUserDeletionException extends RuntimeException {
    public PasswordIncorrectForUserDeletionException() {
        super("Password incorrect.");
    }
}
