package com.taka.tenpo.controller;

import com.taka.tenpo.domain.security.exception.InvalidTokenException;
import com.taka.tenpo.domain.security.exception.InvalidUsernameException;
import com.taka.tenpo.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ InvalidUsernameException.class, InvalidTokenException.class})
    public ResponseEntity<ApiResponse> handleInvalidUsernameAndTokenException(Exception ex) {
        ApiResponse apiResponseError = new ApiResponse(UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(apiResponseError, UNAUTHORIZED);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleArgumentNotValidException(MethodArgumentNotValidException manve) {
        ApiResponse apiResponseError = new ApiResponse(BAD_REQUEST.value(), manve.getGlobalError().getDefaultMessage());
        return new ResponseEntity<>(apiResponseError, BAD_REQUEST);
    }
}
