package com.softwareengineering.exam_registration.exception;

public class AdminNotFoundException extends RuntimeException {

    public AdminNotFoundException(String message) {
        super(message);
    }
}