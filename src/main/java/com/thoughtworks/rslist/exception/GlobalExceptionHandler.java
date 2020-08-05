package com.thoughtworks.rslist.exception;

import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {InvalidParamException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        String errorMessage = null;
        errorMessage = ex.getMessage();

        CommonError commonError = new CommonError();
        commonError.setError(errorMessage);
        logger.error("[LOGGING]: " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}
