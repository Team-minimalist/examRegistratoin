package com.softwareengineering.exam_registration.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice 
public class GlobalExceptionExceptoin {

    private static final Logger Log = LoggerFactory.getLogger(GlobalExceptionExceptoin.class);

    // Spring Security
    @ExceptionHandler(CsrfException.class)
    public String csrfExceptionHandler(CsrfException ex, RedirectAttributes redirectAttribute) {
        Log.warn("CSRF validation failed: {}", ex.getMessage());
        redirectAttribute.addFlashAttribute("message", "Session expired, please login again.");
        return "redirect:/login";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String genericExceptionHandler(Exception ex) {
        Log.error("An error occurred: {}", ex.getMessage(), ex);
        return "500";
    }
}
