package org.uwgb.compsci330.server.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(AuthenticationException.class)
    public Exception handleAllAuthenticationExceptions(AuthenticationException ex) {
        return ex;
    }
}
