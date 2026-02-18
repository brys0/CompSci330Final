package org.uwgb.compsci330.server.exception;

public class UsernameOrPasswordIncorrectException extends RuntimeException {
    public UsernameOrPasswordIncorrectException() {
        super("Username or password is incorrect.");
    }
}
