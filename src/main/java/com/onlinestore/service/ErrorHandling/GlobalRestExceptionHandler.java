package com.onlinestore.service.ErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(GlobalNotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleNotFound(GlobalNotFoundException exc) {
        return buildResponse(HttpStatus.NOT_FOUND, exc.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<GlobalErrorResponse> handleInsufficientStock(InsufficientStockException exc) {
        return buildResponse(HttpStatus.CONFLICT, exc.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<GlobalErrorResponse> handleDuplicate(DuplicateResourceException exc) {
        return buildResponse(HttpStatus.CONFLICT, exc.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalErrorResponse> handleAccessDenied(AccessDeniedException exc) {
        return buildResponse(HttpStatus.FORBIDDEN, exc.getMessage());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<GlobalErrorResponse> handleSpringAccessDenied(
            org.springframework.security.access.AccessDeniedException exc) {
        return buildResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GlobalErrorResponse> handleBadCredentials(BadCredentialsException exc) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalErrorResponse> handleIllegalArgument(IllegalArgumentException exc) {
        return buildResponse(HttpStatus.BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorResponse> handleGeneric(Exception exc) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage());
    }

    private ResponseEntity<GlobalErrorResponse> buildResponse(HttpStatus status, String message) {
        GlobalErrorResponse error = new GlobalErrorResponse(status.value(), message, System.currentTimeMillis());
        return new ResponseEntity<>(error, status);
    }
}
