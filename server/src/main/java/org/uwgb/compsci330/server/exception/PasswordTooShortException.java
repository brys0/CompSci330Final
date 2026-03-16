package org.uwgb.compsci330.server.exception;

import org.uwgb.compsci330.server.ServerConfiguration;

public class PasswordTooShortException extends RuntimeException {

    public PasswordTooShortException() {
        super(String.format("Password must be longer than %d characters.", ServerConfiguration.MIN_PASSWORD_LENGTH));
    }
}
