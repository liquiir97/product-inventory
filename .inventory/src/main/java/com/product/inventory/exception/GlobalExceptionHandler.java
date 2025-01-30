package com.product.inventory.exception;

import com.product.inventory.controller.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorCasess = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(", ");
            errorCasess.add(error.getDefaultMessage());
        }
        // Remove last comma
        if (errorMessage.length() > 0) {
            errorMessage.setLength(errorMessage.length() - 2);
        }
        LOG.debug("REST AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA : ");
        Map<String,List<String>> error = new HashMap<>();
        error.put("message", errorCasess);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        LOG.debug("REST request failed: {}", ex.getMessage());
        List<String> errorCase = new ArrayList<>();
        errorCase.add(ex.getReason());
        Map<String, List<String>> errorCases = new HashMap<>();
        errorCases.put("message", errorCase);
        return ResponseEntity.status(ex.getStatusCode()).body(errorCases);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, List<String>>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex)
    {
        Map<String, List<String>> response = new HashMap<>();
        List<String> errors = new ArrayList<>();
        if (ex.getMessage().contains("Double")) {
            errors.add("Price must be a valid number");
        }
        if (ex.getMessage().contains("Long")) {
            errors.add("Quantity must be a valid number");
        }
        response.put("message", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
