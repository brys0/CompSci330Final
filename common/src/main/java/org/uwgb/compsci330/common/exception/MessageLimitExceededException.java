package org.uwgb.compsci330.common.exception;

import org.uwgb.compsci330.server.ServerConfiguration;

public class MessageLimitExceededException extends RuntimeException {
    public MessageLimitExceededException() {
        super(String.format("You can't fetch more than %d messages in one request.", ServerConfiguration.MAX_MESSAGE_FETCH_LIMIT));
    }
}
