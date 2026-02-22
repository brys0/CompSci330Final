package org.uwgb.compsci330.server.dto.response;

import org.uwgb.compsci330.server.entity.Relationship;
import org.uwgb.compsci330.server.entity.RelationshipStatus;
import org.uwgb.compsci330.server.entity.User;

public class SafeRelationship {
    private SafeUser requester;
    private SafeUser requestee;
    private RelationshipStatus status;

    public SafeRelationship() {}

    public SafeRelationship(Relationship relationship) {
       this(relationship.getRequester(), relationship.getRequestee(), relationship.getStatus());
    }

    public SafeRelationship(User requester, User requestee, RelationshipStatus status) {
        this.requester = new SafeUser(requester);
        this.requestee = new SafeUser(requestee);
        this.status = status;
    }


    public SafeUser getRequester() {
        return requester;
    }

    public SafeUser getRequestee() {
        return requestee;
    }

    public RelationshipStatus getStatus() {
        return status;
    }
}
