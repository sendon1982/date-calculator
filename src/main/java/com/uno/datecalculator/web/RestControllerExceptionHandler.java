package com.uno.datecalculator.web;

import com.uno.datecalculator.web.model.ResponseStatus;
import com.uno.datecalculator.web.model.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>(ResponseStatus.FAILURE.toString(), null);
        responseWrapper.setMessage(ex.getMessage());

        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}