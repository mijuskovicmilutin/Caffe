package com.example.caffe.api.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class ExceptionModel {

    private HttpStatus httpStatus;
    private String message;
    private ZonedDateTime timestamp;
}
