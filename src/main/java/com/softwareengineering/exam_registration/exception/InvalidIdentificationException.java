package com.softwareengineering.exam_registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidIdentificationException extends RuntimeException {
    public InvalidIdentificationException(String message) {
        super(message);
    }
}
