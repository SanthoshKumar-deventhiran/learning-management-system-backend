package com.example.demosecuretwo.Controller;

import com.example.demosecuretwo.Exceptions.PasswordIncorrectException;
import com.example.demosecuretwo.Exceptions.ResourceNotFoundException;
import com.example.demosecuretwo.Exceptions.UserAlreadyExistsException;
import com.example.demosecuretwo.Exceptions.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User Not Found");
        response.put("message", ex.getMessage());
        response.put("suggestion", "Ensure you entered the correct username or email, or create a new account.");
        response.put("status", HttpStatus.NOT_FOUND.name());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<Map<String, String>> handlePasswordIncorrectException(PasswordIncorrectException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Password");
        response.put("message", ex.getMessage());
        response.put("suggestion", "Try again or reset your password if you've forgotten it.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({ UserAlreadyExistsException.class, DataIntegrityViolationException.class })
    public ResponseEntity<Map<String, Object>> handleDuplicateEntryException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Duplicate Entry");

        if (ex instanceof UserAlreadyExistsException) {
            response.put("message", ex.getMessage()); // Custom message from exception
        } else {
            response.put("message", "Email or username already exists. Please use a different one.");
        }

        response.put("suggestion", "Try logging in if you already have an account.");
        response.put("status", HttpStatus.CONFLICT.value()); // Status code 409

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Resource Not Found");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value()); // 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

