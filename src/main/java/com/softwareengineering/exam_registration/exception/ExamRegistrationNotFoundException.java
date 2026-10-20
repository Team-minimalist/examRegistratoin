package com.softwareengineering.exam_registration.exception;

public class ExamRegistrationNotFoundException
        extends RuntimeException {

    public ExamRegistrationNotFoundException(String message) {
        super(message);
    }
}