package org.uwgb.compsci330.common.websocket.model.out.relationship;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.Nullable;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;

public record RelationshipEventPayload(
        RelationshipEventType type,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Nullable SafeRelationship relationship
) {
}
