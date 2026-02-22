package org.uwgb.compsci330.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.server.dto.response.SafeRelationship;
import org.uwgb.compsci330.server.entity.Relationship;
import org.uwgb.compsci330.server.entity.RelationshipStatus;
import org.uwgb.compsci330.server.entity.User;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.repository.RelationshipRepository;
import org.uwgb.compsci330.server.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class RelationshipService {
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
                .stream()
                .findFirst()
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

            return new SafeRelationship(existingRelationship);
        }

        // Create new req
        final User requester = userRepository
                .findUserById(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new InvalidFriendRequestException(userId));

        final Relationship newReq = new Relationship(requester, otherUser);
        relationshipRepository.save(newReq);

        return new SafeRelationship(newReq);
    }

    @Transactional
    public void deleteRelationship(String userId, String otherUserId) {
        final List<Relationship> existingUserRelationships = relationshipRepository.findAllRelationships(userId);

        // Iterate over relationships
        for (Relationship relationship : existingUserRelationships) {
            // Relationship exists, we can delete it.
            if (relationship.getRequestee().getId().equals(otherUserId) || relationship.getRequester().getId().equals(otherUserId)) {
                relationshipRepository.deleteById(relationship.getId());
                return;
            }
        }

        throw new RelationshipDoesNotExistException(otherUserId);
    }

    @Transactional
    public List<SafeRelationship> getRelationships(String userId) {
        return relationshipRepository.findAllSafeRelationships(userId);
    }
}
