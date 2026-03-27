package org.uwgb.compsci330.common.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException create(String username) {
        return new UserAlreadyExistsException(String.format("User with name '%s' already exists.", username));
    }
}
