package com.thoughtworks.rslist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sun.misc.InvalidJarIndexException;

import java.security.InvalidParameterException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidJarIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        String errorMessage;
        CommonError commonError = new CommonError();
        if(ex instanceof MethodArgumentNotValidException){
            errorMessage = "invalid param";
        }else{
            errorMessage = ex.getMessage();
        }
        commonError.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}
