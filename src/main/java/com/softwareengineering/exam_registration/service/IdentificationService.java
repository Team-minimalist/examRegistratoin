package com.softwareengineering.exam_registration.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.exception.InvalidIdentificationException;
import com.softwareengineering.exam_registration.model.IdentificationType;
import com.softwareengineering.exam_registration.util.AgeCalculator;

@Service
public class IdentificationService {

    public void validateIdentification(IdentificationType type, String number) {
        validateIdentification(type, number, null);
    }

    public void validateIdentification(IdentificationType type, String number, LocalDate dateOfBirth) {
        if (type == null) {
            throw new InvalidIdentificationException("Identification type is required");
        }
        if (number == null || number.trim().isEmpty()) {
            throw new InvalidIdentificationException("Identification number is required");
        }

        int age = dateOfBirth != null ? AgeCalculator.calculateAge(dateOfBirth) : 0;
        String cleaned = number.trim().toUpperCase();

        switch (type) {
            case AADHAAR:
                if (!cleaned.matches("^\\d{12}$")) {
                    throw new InvalidIdentificationException("Aadhaar number must be exactly 12 digits");
                }
                break;
            case PAN_CARD:
                if (dateOfBirth != null && age < 18) {
                    throw new InvalidIdentificationException("Applicant must be at least 18 years of age to use PAN Card as primary identification");
                }
                if (!cleaned.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {
                    throw new InvalidIdentificationException("PAN number format is invalid (e.g., ABCDE1234F)");
                }
                break;
            case VOTER_ID:
                if (dateOfBirth != null && age < 18) {
                    throw new InvalidIdentificationException("Voter ID is only valid for applicants aged 18 and above");
                }
                if (!cleaned.matches("^[A-Z0-9]{10}$")) {
                    throw new InvalidIdentificationException("Voter ID must be 10 alphanumeric characters");
                }
                break;
            case DRIVING_LICENCE:
                if (dateOfBirth != null && age < 18) {
                    throw new InvalidIdentificationException("Driving Licence is only valid for applicants aged 18 and above");
                }
                if (!cleaned.matches("^[A-Z0-9]{10,16}$")) {
                    throw new InvalidIdentificationException("Driving licence number must be 10 to 16 alphanumeric characters");
                }
                break;
            case PASSPORT:
                if (!cleaned.matches("^[A-Z0-9]{8,9}$")) {
                    throw new InvalidIdentificationException("Passport number must be 8 or 9 alphanumeric characters");
                }
                break;
            case BIRTH_CERTIFICATE:
                if (cleaned.length() < 6 || cleaned.length() > 25) {
                    throw new InvalidIdentificationException("Birth certificate number must be between 6 and 25 characters");
                }
                break;
            default:
                throw new InvalidIdentificationException("Unsupported identification type: " + type);
        }
    }
}
