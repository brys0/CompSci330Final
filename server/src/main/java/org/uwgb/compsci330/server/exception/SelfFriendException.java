package org.uwgb.compsci330.server.exception;

public class SelfFriendException extends RuntimeException {
    public SelfFriendException() {
        super("You can't friend yourself. Try making friends :)");
    }
}
