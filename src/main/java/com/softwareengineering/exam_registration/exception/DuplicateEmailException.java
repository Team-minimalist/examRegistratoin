package com.softwareengineering.exam_registration.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message){
        super(message);
    }
}
