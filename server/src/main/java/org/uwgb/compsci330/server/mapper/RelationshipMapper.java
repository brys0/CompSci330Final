package org.uwgb.compsci330.server.mapper;

import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.server.entity.relationship.Relationship;

public class RelationshipMapper {
    public static SafeRelationship toSafe(Relationship relationship) {
        return new SafeRelationship(
                relationship.getConversation() != null ? relationship.getConversation().getId() : null,
                UserMapper.toSafe(relationship.getRequester()),
                UserMapper.toSafe(relationship.getRequestee()),
                relationship.getStatus()
        );
    }
}
