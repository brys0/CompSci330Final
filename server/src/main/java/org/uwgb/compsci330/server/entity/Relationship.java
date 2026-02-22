package org.uwgb.compsci330.server.entity;

import jakarta.persistence.*;
import org.uwgb.compsci330.server.dto.response.SafeUser;

@Entity(name = "relationships")
@Table(
        name = "relationships",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"requester", "requestee"})
        }
)
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestee")
    private User requestee;

    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;

    public Relationship() {}

    public Relationship(User requester, User requestee) {
        this.requester = requester;
        this.requestee = requestee;
        this.status = RelationshipStatus.PENDING;
    }

    public boolean isOutgoingRequest(String id) {
        return requester.getId().equals(id) && status == RelationshipStatus.PENDING;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s to %s)", id, status, requester, requestee);
    }

    public User getRequester() {
        return requester;
    }


    public User getRequestee() {
        return requestee;
    }


    public RelationshipStatus getStatus() {
        return status;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }
}
