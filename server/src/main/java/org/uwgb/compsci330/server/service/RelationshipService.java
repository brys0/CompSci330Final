package org.uwgb.compsci330.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.uwgb.compsci330.common.exception.*;
import org.uwgb.compsci330.common.model.response.relationship.RelationshipStatus;
import org.uwgb.compsci330.common.model.response.relationship.SafeRelationship;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipCreatedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipDeletedEvent;
import org.uwgb.compsci330.common.websocket.model.out.relationship.RelationshipPendingEvent;
import org.uwgb.compsci330.server.annotation.FragileSensitiveApi;
import org.uwgb.compsci330.server.annotation.SensitiveApi;
import org.uwgb.compsci330.server.entity.conversation.Conversation;
import org.uwgb.compsci330.server.entity.relationship.Relationship;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.mapper.RelationshipMapper;
import org.uwgb.compsci330.server.repository.RelationshipRepository;
import org.uwgb.compsci330.server.repository.UserRepository;
import org.uwgb.compsci330.server.websocket.event.EventEnvelope;

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

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Transactional
    @FragileSensitiveApi
    public SafeRelationship createRelationship(String userId, String otherUsername) {
        final User otherUser = userRepository
                .findByUsername(otherUsername)
                .stream()
                .findFirst()
                .orElseThrow(() -> InvalidFriendRequestException.create(otherUsername)); // User with that username must not exist.

        // A user is trying to friend the "System" user, bad!
        if (otherUser.isSystemUser()) throw new ReservedUsernameException();

        // A user is trying to friend themselves :(
        if (userId.equals(otherUser.getId())) throw new SelfFriendException();

        Relationship existingRelationship = relationshipRepository
                .findRelationshipByUserAndOtherUser(userId, otherUser.getId())
                .orElse(null);

        // Create new req
        final User requester = userRepository
                .findUserById(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> InvalidFriendRequestException.create(userId));

        if (existingRelationship != null) {
            // If a relationship already exists.
            if (existingRelationship.getStatus() == RelationshipStatus.ACCEPTED) {
                // If the user accepted the request, the friend is the original requester
                if (Objects.equals(existingRelationship.getRequestee().getId(), userId)) throw ExistingRelationshipException.create(existingRelationship.getRequester().getUsername());

                // Otherwise the user is the original requester.
                throw ExistingRelationshipException.create(existingRelationship.getRequestee().getUsername());
            }

            boolean outgoingRequest = existingRelationship.isOutgoingRequest(userId);
            if (outgoingRequest) throw OutgoingRequestAlreadyExistsException.create(existingRelationship.getRequestee().getUsername());
            final Conversation convo =  conversationService.createConversation(Set.of(requester, otherUser));

            // Friendship should be accepted.
            existingRelationship.setConversation(convo);
            existingRelationship.setStatus(RelationshipStatus.ACCEPTED);

            relationshipRepository.save(existingRelationship);

            final SafeRelationship relationship = RelationshipMapper.toSafe(existingRelationship);

            publisher.publishEvent(
                    new EventEnvelope<>(
                            new RelationshipCreatedEvent(relationship),
                            userId,
                            otherUser.getId()
                    )
            );

            return relationship;
        }


        final Relationship newReq = new Relationship(requester, otherUser);
        relationshipRepository.save(newReq);

        final SafeRelationship relationship = RelationshipMapper.toSafe(newReq);

        publisher.publishEvent(
                new EventEnvelope<>(
                        new RelationshipPendingEvent(relationship),
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
                        new EventEnvelope<>(
                                new RelationshipDeletedEvent(RelationshipMapper.toSafe(relationship)),
                                relationship.getRequester().getId(),
                                relationship.getRequestee().getId()
                        )
                );

                return;
            }
        }

        throw RelationshipDoesNotExistException.create(otherUsername);
    }

    @Transactional
    public List<SafeRelationship> getRelationships(String userId) {
        return relationshipRepository.findAllRelationships(userId)
                .stream()
                .map(RelationshipMapper::toSafe)
                .toList();
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
