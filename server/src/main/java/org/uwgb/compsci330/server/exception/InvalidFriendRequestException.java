package org.uwgb.compsci330.server.exception;

public class InvalidFriendRequestException extends RuntimeException {
    public InvalidFriendRequestException(String username) {
        super(String.format("User with username \"%s\" doesn't exist.", username));
    }
}
