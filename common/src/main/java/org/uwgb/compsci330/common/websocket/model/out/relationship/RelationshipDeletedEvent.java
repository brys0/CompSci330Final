package org.uwgb.compsci330.common.websocket.model.out.relationship;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class RelationshipDeletedEvent extends OutboundEvent<SafeRelationship> {
    public RelationshipDeletedEvent(SafeRelationship payload) {
        super(OutboundEventType.RELATIONSHIP_DELETED, payload);
    }
}
