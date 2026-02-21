package org.uwgb.compsci330.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.uwgb.compsci330.server.dto.request.LoginUserRequest;
import org.uwgb.compsci330.server.dto.request.RegisterUserRequest;
import org.uwgb.compsci330.server.dto.request.UserDeleteRequest;
import org.uwgb.compsci330.server.dto.response.ErrorResponse;
import org.uwgb.compsci330.server.dto.response.SafeUser;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Defines all user based APIs, such as logging in, registering, and deleting your account.")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Register a user with a username and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns a jwt token valid for 30 days.",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Username already exists",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Payload was malformed, or your username or password was invalid.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @PostMapping("/register")
    public String register(@RequestBody RegisterUserRequest req) {
        return userService.register(req);
    }


    @Operation(summary = "Login with a given username and password.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns a jwt token valid for 30 days",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Payload was malformed.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @PostMapping("/login")
    public String login(@RequestBody LoginUserRequest req) {
        return userService.login(req);
    }

    @Operation(summary = "Register a user with a username and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns a user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SafeUser.class)
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
    })
    @Parameter(name = "Authorization", description = "JWT Token", required = true, in = ParameterIn.HEADER)
    @GetMapping("/@me")
    public SafeUser getMe(@RequestHeader("Authorization") String auth) {
        return userService.getMe(auth);
    }

    @Operation(summary = "Delete current user with given token and password confirmation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User was deleted"
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

            @ApiResponse(
                    responseCode = "401",
                    description = "Password was incorrect or invalid.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            ),
    })
    @Parameter(name = "Authorization", description = "JWT Token", required = true, in = ParameterIn.HEADER)
    @DeleteMapping("/@me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestHeader("Authorization") String auth, @RequestBody UserDeleteRequest req) {
        userService.deleteUser(auth, req);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = PasswordTooShortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePasswordTooShortException(PasswordTooShortException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = UsernameOrPasswordIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUsernameOrPasswordIncorrectException(UsernameOrPasswordIncorrectException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = UsernameTooLongException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUsernameTooLongException(UsernameTooLongException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = UsernameTooShortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUsernameTooShortException(UsernameTooShortException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = PasswordIncorrectForUserDeletionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlePasswordIncorrectForDeletionException(PasswordIncorrectForUserDeletionException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
