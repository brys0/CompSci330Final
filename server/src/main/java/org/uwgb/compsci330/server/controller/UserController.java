package org.uwgb.compsci330.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.uwgb.compsci330.server.dto.request.LoginUserRequest;
import org.uwgb.compsci330.server.dto.request.RegisterUserRequest;
import org.uwgb.compsci330.server.dto.response.ErrorResponse;
import org.uwgb.compsci330.server.dto.response.SafeUser;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.exception.*;
import org.uwgb.compsci330.server.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterUserRequest req) {
        return userService.register(req);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUserRequest req) {
        return userService.login(req);
    }

    @GetMapping("/@me")
    public SafeUser getMe(@RequestHeader("Authorization") String auth) {
        return userService.getMe(auth);
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
}
