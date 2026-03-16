package org.uwgb.compsci330.server.dto.request;

import lombok.Getter;

public class CreateMessageRequest {
    @Getter
    private String content;
    public CreateMessageRequest() {}

    public CreateMessageRequest(String content) {
        this.content = content;
    }
}