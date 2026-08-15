package com.novaimmo.demo.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(

            ResourceNotFoundException exception,

            HttpServletRequest request
    ) {

        ApiError error =
                new ApiError(
                        404,
                        "NOT_FOUND",
                        exception.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(

            BusinessException exception,

            HttpServletRequest request
    ) {

        ApiError error =
                new ApiError(
                        409,
                        "BUSINESS_ERROR",
                        exception.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(

            MethodArgumentNotValidException exception,

            HttpServletRequest request
    ) {

        String message =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(error ->
                                error.getField()
                                        + " : "
                                        + error.getDefaultMessage()
                        )
                        .orElse(
                                "Données invalides"
                        );

        ApiError error =
                new ApiError(
                        400,
                        "VALIDATION_ERROR",
                        message,
                        request.getRequestURI(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .badRequest()
                .body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(

            Exception exception,

            HttpServletRequest request
    ) {

        ApiError error =
                new ApiError(
                        500,
                        "INTERNAL_SERVER_ERROR",
                        "Une erreur interne est survenue",
                        request.getRequestURI(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(error);
    }
}