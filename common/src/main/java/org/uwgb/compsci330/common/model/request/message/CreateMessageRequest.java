package org.uwgb.compsci330.common.model.request.message;

import lombok.Getter;

public class CreateMessageRequest {
    @Getter
    private String content;
    public CreateMessageRequest() {}

    public CreateMessageRequest(String content) {
        this.content = content;
    }
}