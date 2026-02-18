package org.uwgb.compsci330.server.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("You are not authorized to access this resource at this time.");
    }
}
