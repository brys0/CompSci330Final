package org.uwgb.compsci330.common.exception;

import org.uwgb.compsci330.common.Configuration;

public class UsernameTooShortException extends RuntimeException {
    public UsernameTooShortException(String message) {
        super(message);
    }

    public static UsernameTooShortException create(String username) {
        return new UsernameTooShortException(String.format("Username '%s' must be longer than %d characters.", username, Configuration.MIN_USERNAME_LENGTH));
    }
}
