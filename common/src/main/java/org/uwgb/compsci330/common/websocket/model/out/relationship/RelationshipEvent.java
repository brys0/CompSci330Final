package org.uwgb.compsci330.common.websocket.model.out.relationship;

import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.websocket.model.event.OutboundEventWithIdentity;
import org.uwgb.compsci330.common.websocket.model.out.OutboundEventType;

import java.util.Collection;

public class RelationshipEvent extends OutboundEventWithIdentity {
    public RelationshipEvent(RelationshipEventType type, SafeRelationship payload, String... users) {
        super(OutboundEventType.RELATIONSHIP, new RelationshipEventPayload(type, payload), users);
    }

    public RelationshipEvent(RelationshipEventType type, SafeRelationship payload, Collection<String> users) {
        super(OutboundEventType.RELATIONSHIP, new RelationshipEventPayload(type, payload), users);
    }
}
