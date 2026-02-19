package org.uwgb.compsci330.server.dto.response;

public class ErrorResponse {
    private String message;

    @SuppressWarnings("unused")
    public ErrorResponse() {}

    public ErrorResponse(String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }
}
