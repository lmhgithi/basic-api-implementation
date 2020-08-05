package com.thoughtworks.rslist.exception;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sun.misc.InvalidJarIndexException;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidJarIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        String errorMessage = null;

        if(ex instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            if(methodArgumentNotValidException.getBindingResult().getTarget() instanceof RsEvent){
                errorMessage = "invalid param";
            }
            if(methodArgumentNotValidException.getBindingResult().getTarget() instanceof User){
                errorMessage = "invalid user";
            }
        }else{
            errorMessage = ex.getMessage();
        }
        CommonError commonError = new CommonError("");
        if(errorMessage == null){
            errorMessage = "common error";
        }
        commonError.setError(errorMessage);
        logger.error("[LOGGING]: " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}
