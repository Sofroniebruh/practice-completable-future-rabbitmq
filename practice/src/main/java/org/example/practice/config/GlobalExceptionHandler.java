package org.example.practice.config;

import org.example.practice.config.exceptions.InternalErrorException;
import org.example.practice.config.exceptions.ProductNotFoundException;
import org.example.practice.config.records.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> bankAccountNotFound(ProductNotFoundException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage(), Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ErrorResponse> internalError(InternalErrorException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage(), Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
