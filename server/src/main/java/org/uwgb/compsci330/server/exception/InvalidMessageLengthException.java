package org.uwgb.compsci330.server.exception;

public class InvalidMessageLengthException extends RuntimeException {
    public InvalidMessageLengthException() {
        super("Message length must exceed 0 chars, and be visible characters, not just invisible.");
    }
}
