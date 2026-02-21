package org.uwgb.compsci330.server.exception;

public class PasswordIncorrectForUserDeletionException extends RuntimeException {
    public PasswordIncorrectForUserDeletionException() {
        super("Password incorrect.");
    }
}
