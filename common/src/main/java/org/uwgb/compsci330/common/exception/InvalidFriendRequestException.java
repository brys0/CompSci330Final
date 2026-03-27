package org.uwgb.compsci330.common.exception;

public class InvalidFriendRequestException extends RuntimeException {
    public InvalidFriendRequestException(String message) {
        super(message);
    }

    public static InvalidFriendRequestException create(String username) {
        return new InvalidFriendRequestException(String.format("User with username \"%s\" doesn't exist.", username));
    }
}
