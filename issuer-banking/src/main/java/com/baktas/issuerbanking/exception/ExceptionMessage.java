package com.baktas.issuerbanking.exception;

import org.springframework.http.HttpStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ExceptionMessage {
    String code();
    String reason();
    HttpStatus httpStatus() default HttpStatus.INTERNAL_SERVER_ERROR;
}