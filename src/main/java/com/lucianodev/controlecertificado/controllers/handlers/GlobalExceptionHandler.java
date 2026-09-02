package com.lucianodev.controlecertificado.controllers.handlers;

import com.lucianodev.controlecertificado.dtos.CustomError;
import com.lucianodev.controlecertificado.exceptions.ConflictException;
import com.lucianodev.controlecertificado.exceptions.ForbiddenException;
import com.lucianodev.controlecertificado.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomError> conflict(ConflictException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String msgLimpa = e.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos campos");
        return builderResponse(HttpStatus.UNPROCESSABLE_CONTENT, msgLimpa, request);
    }

    private ResponseEntity<CustomError> builderResponse(HttpStatus status, String msg, HttpServletRequest request) {
        CustomError errorDto = new CustomError(Instant.now(), status.value(), msg, request.getRequestURI());
        return ResponseEntity.status(status).body(errorDto);
    }
}
