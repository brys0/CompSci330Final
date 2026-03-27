package org.uwgb.compsci330.common.exception;

import org.uwgb.compsci330.common.Configuration;

public class UsernameTooLongException extends RuntimeException {
    public UsernameTooLongException(String message) {
        super(message);
    }

    public static UsernameTooLongException create(String username) {
        return new UsernameTooLongException(String.format("Username '%s' must be longer than %d characters.", username, Configuration.MAX_USERNAME_LENGTH));
    }
}
