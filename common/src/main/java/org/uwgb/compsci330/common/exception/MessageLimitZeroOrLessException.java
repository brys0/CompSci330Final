package org.uwgb.compsci330.common.exception;

public class MessageLimitZeroOrLessException extends RuntimeException {
    public MessageLimitZeroOrLessException() {
        super("You can't fetch zero, or negative messages.");
    }
}
