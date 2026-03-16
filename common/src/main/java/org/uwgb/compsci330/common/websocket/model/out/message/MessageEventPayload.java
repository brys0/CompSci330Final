package org.uwgb.compsci330.common.websocket.model.out.message;

import com.fasterxml.jackson.annotation.JsonInclude;

public record MessageEventPayload(
        MessageEventType type,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object payload
) {


}
