package org.uwgb.compsci330.server.websocket.dto.out.relationship;

import org.uwgb.compsci330.server.dto.response.SafeRelationship;
import org.uwgb.compsci330.server.websocket.dto.out.OutboundEventType;
import org.uwgb.compsci330.server.websocket.event.OutboundEventWithIdentity;

import java.util.List;

public class RelationshipEvent extends OutboundEventWithIdentity {
    public RelationshipEvent(RelationshipEventType type, SafeRelationship payload, String... users) {
        super(OutboundEventType.RELATIONSHIP, new RelationshipEventPayload(type, payload), users);
    }

    public RelationshipEvent(RelationshipEventType type, SafeRelationship payload, List<String> users) {
        super(OutboundEventType.RELATIONSHIP, new RelationshipEventPayload(type, payload), users);
    }
}
