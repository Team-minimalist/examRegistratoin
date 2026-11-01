package com.softwareengineering.exam_registration.service;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.model.IdentificationType;

@Service
public class IdentificationService {

    public void validateIdentification(IdentificationType type, String number) {
        if (type == null) {
            throw new InvalidRegistrationException("Identification type is required");
        }
        if (number == null || number.trim().isEmpty()) {
            throw new InvalidRegistrationException("Identification number is required");
        }

        String cleaned = number.trim().toUpperCase();

        switch (type) {
            case AADHAAR:
                if (!cleaned.matches("^\\d{12}$")) {
                    throw new InvalidRegistrationException("Aadhaar number must be exactly 12 digits");
                }
                break;
            case PAN_CARD:
                if (!cleaned.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {
                    throw new InvalidRegistrationException("PAN number must follow standard format (e.g., ABCDE1234F)");
                }
                break;
            case PASSPORT:
                if (!cleaned.matches("^[A-Z0-9]{8,9}$")) {
                    throw new InvalidRegistrationException("Passport number must be 8 or 9 alphanumeric characters");
                }
                break;
            case VOTER_ID:
                if (!cleaned.matches("^[A-Z0-9]{10}$")) {
                    throw new InvalidRegistrationException("Voter ID must be 10 alphanumeric characters");
                }
                break;
            case DRIVING_LICENCE:
                if (!cleaned.matches("^[A-Z0-9]{10,16}$")) {
                    throw new InvalidRegistrationException("Driving licence number must be 10 to 16 alphanumeric characters");
                }
                break;
            case BIRTH_CERTIFICATE:
                if (cleaned.length() < 6 || cleaned.length() > 25) {
                    throw new InvalidRegistrationException("Birth certificate number must be between 6 and 25 characters");
                }
                break;
            default:
                throw new InvalidRegistrationException("Unsupported identification type: " + type);
        }
    }
}

