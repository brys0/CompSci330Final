package org.uwgb.compsci330.server.exception;

import org.uwgb.compsci330.server.Configuration;

public class UsernameTooLongException extends RuntimeException {
    public UsernameTooLongException(String username) {
        super(String.format("Username '%s' must be longer than %d characters.", username, Configuration.MAX_USERNAME_LENGTH));
    }
}
