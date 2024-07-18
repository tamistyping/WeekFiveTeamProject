package com.sparta.zmsb.weekfiveteamproject.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", e.getMessage(), request.getRequestURL().toString()), HttpStatus.BAD_REQUEST);
    }

    private record ErrorResponse(Object errorDetails, String errorCode, String url){}
}
