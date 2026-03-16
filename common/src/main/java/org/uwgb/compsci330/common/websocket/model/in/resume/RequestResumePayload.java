package org.uwgb.compsci330.common.websocket.model.in.resume;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestResumePayload(
        @JsonProperty("sq")
        long seq
) {}