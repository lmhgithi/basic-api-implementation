package com.thoughtworks.rslist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.InvalidJarIndexException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidJarIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        String errorMessage;
        CommenError commenError = new CommenError();
//        if(ex instanceof InvalidJarIndexException){
//            errorMessage = ex.getMessage();
//        }
        errorMessage = ex.getMessage();
        commenError.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commenError);
    }
}
