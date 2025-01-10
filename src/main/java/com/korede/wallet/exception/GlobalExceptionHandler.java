package com.korede.wallet.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({WalletException.class})
    public ResponseEntity<HttpBaseResponse<String>> walletEx(WalletException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(
                        HttpBaseResponse.<String>builder()
                                .message("wallet Exception")
                                .timestamp(LocalDateTime.now())
                                .errors(List.of(ex.getMessage()))
                                .build());
    }




    @ExceptionHandler({BindException.class})
    public ResponseEntity<HttpBaseResponse<Violation>> raiseForValidationErrors(BindException be) {
        var errors = new ArrayList<Violation>();
        be.getBindingResult()
                .getAllErrors()
                .forEach(
                        cv ->
                                errors.add(
                                        Violation.builder()
                                                .prop(
                                                        String.format(
                                                                "%s.%s",
                                                                cv.getObjectName(),
                                                                ((FieldError) cv).getField()))
                                                .error(cv.getDefaultMessage())
                                                .build()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        HttpBaseResponse.<Violation>builder()
                                .message("Validation Exception")
                                .cause("Input data validation errors")
                                .timestamp(LocalDateTime.now())
                                .errors(errors)
                                .build());
    }



    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<HttpBaseResponse<Violation>> raiseForConstraintValidationErrors(
            ConstraintViolationException be) {
        var violations =
                be.getConstraintViolations().stream()
                        .map(
                                cv ->
                                        Violation.builder()
                                                .prop(cv.getPropertyPath().toString())
                                                .error(cv.getMessage())
                                                .build())
                        .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        HttpBaseResponse.<Violation>builder()
                                .cause("Input data validation errors")
                                .message("Validation Exception")
                                .timestamp(LocalDateTime.now())
                                .errors(violations)
                                .build());
    }


    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<HttpBaseResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        HttpBaseResponse.<String>builder()
                                .message("Invalid username or password")
                                .timestamp(LocalDateTime.now())
                                .errors(List.of(ex.getMessage()))
                                .build());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<HttpBaseResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        HttpBaseResponse.<String>builder()
                                .message("Resource not found")
                                .cause(ex.getMessage())
                                .timestamp(LocalDateTime.now())
                                .errors(List.of(ex.getMessage()))
                                .build());
    }
    @Getter
    @Jacksonized
    @Builder
    public static class Violation implements Serializable {
        String prop;
        String error;
    }
}