package org.uwgb.compsci330.common.websocket.model.out.relationship;

public enum RelationshipEventType {
    // Defines a new pending relationship
    RELATIONSHIP_PENDING,

    // Defines either an incoming, or outgoing request was accepted.
    RELATIONSHIP_ACCEPTED,


    // Defines a previously pending, or existing relationship was removed.
    RELATIONSHIP_DELETED,
}
