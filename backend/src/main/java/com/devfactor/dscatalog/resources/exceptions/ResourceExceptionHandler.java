package com.devfactor.dscatalog.resources.exceptions;

import com.devfactor.dscatalog.services.exceptions.ResourceDatabaseIntegrityException;
import com.devfactor.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Resource not found");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceDatabaseIntegrityException.class)
    public ResponseEntity<StandardError> ResourceDatabaseIntegrityException(ResourceDatabaseIntegrityException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Database exception");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> MethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError standardError = new ValidationError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(status.value());
        standardError.setError("Payload exception");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());
        for (FieldError error : exception.getBindingResult().getFieldErrors()){
            standardError.addError(error.getField(), error.getDefaultMessage());
        }
        exception.getBindingResult().getFieldErrors();
        return ResponseEntity.status(status).body(standardError);
    }
}
