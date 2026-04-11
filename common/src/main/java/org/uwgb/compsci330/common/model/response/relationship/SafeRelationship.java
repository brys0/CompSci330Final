package org.uwgb.compsci330.common.model.response.relationship;


import lombok.Getter;
import org.uwgb.compsci330.common.model.response.user.SafeUser;

public class SafeRelationship {

    @Getter
    private SafeUser requester;

    @Getter
    private SafeUser requestee;

    @Getter
    private RelationshipStatus status;

    public SafeRelationship() {}

    public SafeRelationship(
            SafeUser requester,
            SafeUser requestee,
            RelationshipStatus status
    ) {
        this.requester = requester;
        this.requestee = requestee;
        this.status = status;
    }
}
