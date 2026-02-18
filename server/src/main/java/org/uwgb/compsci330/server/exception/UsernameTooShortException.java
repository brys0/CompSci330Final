package org.uwgb.compsci330.server.exception;

import org.uwgb.compsci330.server.Configuration;

public class UsernameTooShortException extends RuntimeException {
    public UsernameTooShortException(String username) {
        super(String.format("Username '%s' must be longer than %d characters.", username, Configuration.MIN_USERNAME_LENGTH));
    }
}
