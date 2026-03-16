package org.uwgb.compsci330.server.exception;

import org.uwgb.compsci330.server.ServerConfiguration;

public class MaxMessageLengthExceededException extends RuntimeException {
    public MaxMessageLengthExceededException() {
        super(String.format("Your message length exceeds limit of %d chars", ServerConfiguration.MAX_MESSAGE_LENGTH));
    }
}
