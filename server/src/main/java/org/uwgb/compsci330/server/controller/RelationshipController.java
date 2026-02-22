package org.uwgb.compsci330.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.uwgb.compsci330.server.dto.response.ErrorResponse;
import org.uwgb.compsci330.server.dto.response.SafeRelationship;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.service.RelationshipService;

import java.util.List;

@RestController
@RequestMapping("/users/@me/relationships")
@Tag(name = "Relationships", description = "Defines all relationship based APIs, such as creating, rejecting, and viewing current user relationships.")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;


    @Operation(summary = "Get all relationships (active/pending)")
    @Parameter(in = ParameterIn.HEADER, required = true, name = "Authorization", schema = @Schema(type = "jwt-token"))
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns an array of relationships.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(
                                                            implementation = SafeRelationship.class
                                                    )
                                            )
                                    )
                            }
                    ),

                    @ApiResponse(
                            responseCode = "401",
                            description = "Authorization token was not provided or invalid.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class)
                                    )
                            }
                    ),
            }
    )
    @GetMapping
    public List<SafeRelationship> getRelationships(Authentication auth) {
        return relationshipService.getRelationships(auth.getName());
    }

    @Operation(summary = "Create or accept incoming relationship")
    @Parameter(in = ParameterIn.PATH, name = "username", schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.HEADER, required = true, name = "Authorization", schema = @Schema(type = "jwt-token"))
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Returns the new or accepted relationship.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = SafeRelationship.class)
                                    )
                            }
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "InvalidFriendRequestException",
                                                            summary = "The person you were trying to friend doesn't exist."
                                                    ),
                                                    @ExampleObject(
                                                            name = "SelfFriendException",
                                                            summary = "You shouldn't have to friend yourself :("
                                                    ),
                                                    @ExampleObject(
                                                            name = "ExistingRelationshipException",
                                                            summary = "An incoming or accepted relationship already exists."
                                                    ),
                                                    @ExampleObject(
                                                            name = "OutgoingRequestAlreadyExistsException",
                                                            summary = "An outgoing request already exists."
                                                    ),
                                            }
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Authorization token was not provided or invalid.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class)
                                    )
                            }
                    ),
            }
    )
    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public SafeRelationship addRelationship(@PathVariable String username, Authentication auth) {
        return relationshipService.createRelationship(auth.getName(), username);
    }

    @Operation(summary = "Delete or reject incoming relationship")
    @Parameter(in = ParameterIn.PATH, name = "userId", schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.HEADER, required = true, name = "Authorization", schema = @Schema(type = "jwt-token"))
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Relationship deleted successfully"
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Relationship with that user does not exist.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Authorization token was not provided or invalid.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class)
                                    )
                            }
                    ),
            }
    )
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRelationship(@PathVariable String userId, Authentication auth) {
        relationshipService.deleteRelationship(auth.getName(), userId);
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

    @ExceptionHandler(value = RelationshipDoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRelationshipDoesNotExistException(RelationshipDoesNotExistException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
