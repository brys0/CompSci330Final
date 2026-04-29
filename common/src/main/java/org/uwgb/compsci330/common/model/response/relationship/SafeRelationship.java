package org.uwgb.compsci330.common.model.response.relationship;


import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.uwgb.compsci330.common.model.response.user.SafeUser;

public class SafeRelationship {

    @Getter
    private SafeUser requester;

    @Getter
    private SafeUser requestee;

    @Getter
    private RelationshipStatus status;

    @Getter
    @Nullable
    private String conversationId;

    public SafeRelationship() {}

    public SafeRelationship(
            @Nullable String conversationId,
            SafeUser requester,
            SafeUser requestee,
            RelationshipStatus status
    ) {
        this.conversationId = conversationId;
        this.requester = requester;
        this.requestee = requestee;
        this.status = status;
    }
}
