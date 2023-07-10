package com.erick.webfluxcourse.exception;

import com.erick.webfluxcourse.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Mono<StandardError>> duplicateKeyException(DuplicateKeyException e,
                                                                     ServerHttpRequest request) {
        return ResponseEntity.badRequest()
                .body(Mono.just(
                        StandardError.builder()
                                .timestamp(now())
                                .path(request.getPath().toString())
                                .status(BAD_REQUEST.value())
                                .error(BAD_REQUEST.getReasonPhrase())
                                .message(verifyDuplicateKey(e.getMessage()))
                                .build()
                ));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> validationError(WebExchangeBindException e,
                                                                 ServerHttpRequest request) {
        ValidationError error = new ValidationError(
                now(), request.getPath().toString(),
                BAD_REQUEST.value(), "Validation error", "Error on validation attributes"
        );

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(Mono.just(error));
    }
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Mono<StandardError>> objectNotFoundException(ObjectNotFoundException e,
                                                                       ServerHttpRequest request) {
        return ResponseEntity.status(NOT_FOUND)
                .body(Mono.just(
                        StandardError.builder()
                                .timestamp(now())
                                .path(request.getPath().toString())
                                .status(NOT_FOUND.value())
                                .error(NOT_FOUND.getReasonPhrase())
                                .message(e.getMessage())
                                .build()
                ));
    }

    private String verifyDuplicateKey(String message) {
        if (message.contains("email dup key")) {
            return "E-mail already registered!";
        }
        return "Dup key exception.";
    }

}
