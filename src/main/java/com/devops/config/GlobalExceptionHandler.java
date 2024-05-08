package com.devops.config;

import com.devops.exceptions.DataAlreadyExistsException;
import com.devops.exceptions.DataNotFoundException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public Mono<String> dataAlreadyExist(DataAlreadyExistsException e) {
        return Mono.just("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Mono<String> dataNotFound(DataNotFoundException e) {
        return Mono.just("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Mono<String> authException(AuthenticationException e) {
        return Mono.just(e.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Mono<String> accessDenied(ResponseStatusException e) {
        return Mono.just(e.getMessage());
    }

    @ExceptionHandler(NotImplementedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<String> serviceUnavailable(NotImplementedException e) {
        return Mono.just(e.getMessage());
    }
}
