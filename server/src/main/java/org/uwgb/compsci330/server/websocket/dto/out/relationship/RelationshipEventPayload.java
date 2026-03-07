package org.uwgb.compsci330.server.websocket.dto.out.relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import org.uwgb.compsci330.server.dto.response.SafeRelationship;

public record RelationshipEventPayload(
        RelationshipEventType type,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Nullable SafeRelationship relationship
) {
}
