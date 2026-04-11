package org.uwgb.compsci330.common.websocket.model.out.message.delete;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

public record MessageDeleteEventPayload(
        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        String messageId
) {}