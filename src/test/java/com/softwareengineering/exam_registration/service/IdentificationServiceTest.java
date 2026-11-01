package com.softwareengineering.exam_registration.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.model.IdentificationType;

class IdentificationServiceTest {

    private IdentificationService identificationService;

    @BeforeEach
    void setUp() {
        identificationService = new IdentificationService();
    }

    @Test
    void testValidAadhaar() {
        assertDoesNotThrow(() -> identificationService.validateIdentification(IdentificationType.AADHAAR, "123456789012"));
    }

    @Test
    void testInvalidAadhaar() {
        assertThrows(InvalidRegistrationException.class, () ->
                identificationService.validateIdentification(IdentificationType.AADHAAR, "12345"));
    }

    @Test
    void testValidPan() {
        assertDoesNotThrow(() -> identificationService.validateIdentification(IdentificationType.PAN_CARD, "ABCDE1234F"));
    }

    @Test
    void testInvalidPan() {
        assertThrows(InvalidRegistrationException.class, () ->
                identificationService.validateIdentification(IdentificationType.PAN_CARD, "12345ABCDE"));
    }

    @Test
    void testNullIdentificationType() {
        assertThrows(InvalidRegistrationException.class, () ->
                identificationService.validateIdentification(null, "12345"));
    }
}

