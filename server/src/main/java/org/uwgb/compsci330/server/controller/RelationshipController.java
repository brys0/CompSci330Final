package org.uwgb.compsci330.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.uwgb.compsci330.server.dto.response.ErrorResponse;
import org.uwgb.compsci330.server.dto.response.SafeRelationship;
import org.uwgb.compsci330.server.dto.response.SafeUser;
import org.uwgb.compsci330.server.entity.Relationship;
import org.uwgb.compsci330.server.exception.ExistingRelationshipException;
import org.uwgb.compsci330.server.exception.InvalidFriendRequestException;
import org.uwgb.compsci330.server.exception.OutgoingRequestAlreadyExistsException;
import org.uwgb.compsci330.server.exception.SelfFriendException;
import org.uwgb.compsci330.server.service.RelationshipService;

import java.util.List;

@RestController
@RequestMapping("/users/@me/relationships")
@Tag(name = "Relationships", description = "Defines all relationship based APIs, such as creating, rejecting, and viewing current user relationships.")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;


    @GetMapping
    public List<SafeRelationship> getRelationships(Authentication auth) {
        return relationshipService.getRelationships(auth.getName());
    }

    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public SafeRelationship addRelationship(@PathVariable String username, Authentication auth) {
        return relationshipService.createRelationship(auth.getName(), username);
    }

    @ExceptionHandler(value = InvalidFriendRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFriendRequestException(InvalidFriendRequestException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = ExistingRelationshipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExistingRelationshipException(ExistingRelationshipException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = OutgoingRequestAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOutgoingRequestAlreadyExistsException(OutgoingRequestAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = SelfFriendException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSelfFriendException(SelfFriendException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
