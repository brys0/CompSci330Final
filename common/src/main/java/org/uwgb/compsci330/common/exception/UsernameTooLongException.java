package org.uwgb.compsci330.common.exception;

import org.uwgb.compsci330.server.ServerConfiguration;

public class UsernameTooLongException extends RuntimeException {
    public UsernameTooLongException(String username) {
        super(String.format("Username '%s' must be longer than %d characters.", username, ServerConfiguration.MAX_USERNAME_LENGTH));
    }
}
