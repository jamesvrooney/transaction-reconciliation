package com.jamesvrooney.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handle(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String errorMessage = "";

        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            violations.forEach(violation -> builder.append(" " + violation.getMessage()));

            errorMessage = builder.toString();
        } else {
            errorMessage = "ConstraintViolationException occurred.";
        }

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handle(HttpMessageNotReadableException exception) {

        String errorMessage = exception.getMessage();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
