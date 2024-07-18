package com.sparta.zmsb.weekfiveteamproject.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", e.getMessage(), request.getRequestURL().toString()), HttpStatus.NOT_FOUND);
    }

    private record ErrorResponse(Object errorDetails, String errorCode, String url){}
}
