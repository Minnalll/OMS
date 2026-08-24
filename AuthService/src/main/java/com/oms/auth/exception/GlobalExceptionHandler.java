package com.oms.auth.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String,Object>> handleDuplicate(
            ResourceAlreadyExistsException ex){

        Map<String,Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());

        response.put("status", HttpStatus.CONFLICT.value());

        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);

    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidCredentials(

            InvalidCredentialsException ex){

        Map<String,Object> response = new HashMap<>();

        response.put("status",401);

        response.put("message",ex.getMessage());

        response.put("timestamp",LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)

                .body(response);

    }

    @ExceptionHandler(
            InvalidRefreshTokenException.class)
    public ResponseEntity<Map<String,Object>>
    handleRefreshToken(

            InvalidRefreshTokenException ex){

        Map<String,Object> response = new HashMap<>();

        response.put("status",401);

        response.put("message",ex.getMessage());

        response.put("timestamp",LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)

                .body(response);

    }

}