package org.uwgb.compsci330.common.exception;

import org.uwgb.compsci330.common.Configuration;

public class MaxMessageLengthExceededException extends RuntimeException {
    public MaxMessageLengthExceededException() {
        super(String.format("Your message length exceeds limit of %d chars", Configuration.MAX_MESSAGE_LENGTH));
    }
}
