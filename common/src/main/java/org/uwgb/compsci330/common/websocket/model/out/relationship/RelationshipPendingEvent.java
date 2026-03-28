package org.uwgb.compsci330.common.websocket.model.out.relationship;

import lombok.NoArgsConstructor;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEvent;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

@NoArgsConstructor
public class RelationshipPendingEvent extends OutboundEvent<SafeRelationship> {
    public RelationshipPendingEvent(SafeRelationship payload) {
        super(OutboundEventType.RELATIONSHIP_PENDING, payload);
    }
}
