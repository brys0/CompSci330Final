package org.uwgb.compsci330.common.exception;

import org.uwgb.compsci330.common.Configuration;

public class PasswordTooShortException extends RuntimeException {

    public PasswordTooShortException() {
        super(String.format("Password must be longer than %d characters.", Configuration.MIN_PASSWORD_LENGTH));
    }
}
