package org.uwgb.compsci330.common.model.response;

public class ErrorResponse {
    private String type;
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