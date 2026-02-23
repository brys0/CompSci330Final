package org.uwgb.compsci330.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uwgb.compsci330.server.dto.response.SafeRelationship;
import org.uwgb.compsci330.server.entity.Relationship;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, String> {
    @Query("SELECT r FROM relationships r WHERE r.requestee.id = :userId OR r.requester.id = :userId")
    List<Relationship> findAllRelationships(String userId);

    @Query("SELECT new org.uwgb.compsci330.server.dto.response.SafeRelationship(r.requester, r.requestee, r.status) FROM relationships r WHERE r.requestee.id = :userId OR r.requester.id = :userId")
    List<SafeRelationship> findAllSafeRelationships(String userId);

    // TODO: Use Optional<Relationship> instead of list return
    @Query("SELECT r FROM relationships r WHERE (r.requester.id = :userId OR r.requestee.id = :userId) AND (r.requester.id = :otherUserId OR r.requestee.id = :otherUserId)")
    List<Relationship> findRelationshipByUserAndOtherUser(String userId, String otherUserId);
}
