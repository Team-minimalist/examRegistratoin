package com.softwareengineering.exam_registration.exception;

public class DuplicateIdentificationException extends RuntimeException {

    public DuplicateIdentificationException(String message) {
        super(message);
    }
}