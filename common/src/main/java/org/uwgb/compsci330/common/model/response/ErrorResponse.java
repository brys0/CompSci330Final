package org.uwgb.compsci330.common.model.response;

import lombok.Getter;

public class ErrorResponse {
    @Getter
    private String type;
    @Getter
    private String message;

    @SuppressWarnings("unused")
    public ErrorResponse() {}

    public ErrorResponse(Exception exception) {
        this.type = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }
}