package org.uwgb.compsci330.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.server.annotation.SensitiveApi;
import org.uwgb.compsci330.server.dto.response.SafeRelationship;
import org.uwgb.compsci330.server.entity.relationship.Relationship;
import org.uwgb.compsci330.server.entity.relationship.RelationshipStatus;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.repository.RelationshipRepository;
import org.uwgb.compsci330.server.repository.UserRepository;
import org.uwgb.compsci330.server.websocket.dto.out.relationship.RelationshipEvent;
import org.uwgb.compsci330.server.websocket.dto.out.relationship.RelationshipEventType;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RelationshipService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private UserRepository userRepository;

    public SafeRelationship createRelationship(String userId, String otherUsername) {
        final User otherUser = userRepository
                .findByUsername(otherUsername)
                .stream()
                .findFirst()
                .orElseThrow(() -> new InvalidFriendRequestException(otherUsername)); // User with that username must not exist.

        // A user is trying to friend themselves :(
        if (userId.equals(otherUser.getId())) throw new SelfFriendException();

        Relationship existingRelationship = relationshipRepository
                .findRelationshipByUserAndOtherUser(userId, otherUser.getId())
                .orElse(null);

        if (existingRelationship != null) {
            // If a relationship already exists.
            if (existingRelationship.getStatus() == RelationshipStatus.ACCEPTED) {
                // If the user accepted the request, the friend is the original requester
                if (Objects.equals(existingRelationship.getRequestee().getId(), userId)) throw new ExistingRelationshipException(existingRelationship.getRequester().getUsername());

                // Otherwise the user is the original requester.
                throw new ExistingRelationshipException(existingRelationship.getRequestee().getUsername());
            }

            boolean outgoingRequest = existingRelationship.isOutgoingRequest(userId);
            if (outgoingRequest) throw new OutgoingRequestAlreadyExistsException(existingRelationship.getRequestee().getUsername());

            // Friendship should be accepted.
            existingRelationship.setStatus(RelationshipStatus.ACCEPTED);
            relationshipRepository.save(existingRelationship);

            final SafeRelationship relationship = new SafeRelationship(existingRelationship);

            publisher.publishEvent(
                    new RelationshipEvent(
                            RelationshipEventType.RELATIONSHIP_ACCEPTED,
                            relationship,
                            userId,
                            otherUser.getId()
                    )
            );

            return relationship;
        }

        // Create new req
        final User requester = userRepository
                .findUserById(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new InvalidFriendRequestException(userId));

        final Relationship newReq = new Relationship(requester, otherUser);
        relationshipRepository.save(newReq);

        final SafeRelationship relationship = new SafeRelationship(newReq);
        publisher.publishEvent(
                new RelationshipEvent(
                        RelationshipEventType.RELATIONSHIP_PENDING,
                        relationship,
                        userId,
                        otherUser.getId()
                )
        );

        return relationship;
    }

    @Transactional
    public void deleteRelationship(String userId, String otherUsername) {
        final List<Relationship> existingUserRelationships = relationshipRepository.findAllRelationships(userId);

        // Iterate over relationships
        for (Relationship relationship : existingUserRelationships) {
            // Relationship exists, we can delete it.
            if (relationship.getRequestee().getUsername().equals(otherUsername) || relationship.getRequester().getUsername().equals(otherUsername)) {
                relationshipRepository.deleteById(relationship.getId());
                publisher.publishEvent(
                        new RelationshipEvent(
                                RelationshipEventType.RELATIONSHIP_DELETED,
                                new SafeRelationship(relationship),
                                relationship.getRequester().getId(),
                                relationship.getRequestee().getId()
                        )
                );
                return;
            }
        }

        throw new RelationshipDoesNotExistException(otherUsername);
    }

    @Transactional
    public List<SafeRelationship> getRelationships(String userId) {
        return relationshipRepository.findAllSafeRelationships(userId);
    }

    // Get all users for all relationships of a user, besides that users.
    @Transactional
    @SensitiveApi
    public Set<String> getRelationshipPeers(String userId) {
        List<SafeRelationship> relationships = this.getRelationships(userId);

        return relationships.stream().map(rel -> Objects.equals(rel.getRequester().getId(), userId)
                        ? rel.getRequestee().getId()
                        : rel.getRequester().getId())
        .collect(Collectors.toSet());
    }
}
