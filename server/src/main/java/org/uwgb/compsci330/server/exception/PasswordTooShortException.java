package org.uwgb.compsci330.server.exception;

import org.uwgb.compsci330.server.Configuration;

public class PasswordTooShortException extends RuntimeException {

    public PasswordTooShortException() {
        super(String.format("Password must be longer than %d characters.", Configuration.MIN_PASSWORD_LENGTH));
    }
}
