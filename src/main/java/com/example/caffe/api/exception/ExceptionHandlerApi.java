package com.example.caffe.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionHandlerApi {

    @ExceptionHandler(value = {ExceptionOk.class})
    public ResponseEntity<Object> exceptionHandleOk (ExceptionOk e){
        ExceptionModel exceptionModel = new ExceptionModel(
                HttpStatus.OK,
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exceptionModel, HttpStatus.OK);
    }

    @ExceptionHandler(value = {ExceptionBadRequest.class})
    public ResponseEntity<Object> exceptionHandlerBadRequest (ExceptionBadRequest e){
        ExceptionModel exceptionModel = new ExceptionModel(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<> (exceptionModel, HttpStatus.BAD_REQUEST);
    }
}
