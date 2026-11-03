package com.softwareengineering.exam_registration.util;

import java.security.SecureRandom;
import java.time.Year;

public class RegistrationNumberGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private RegistrationNumberGenerator() {
    }

    public static String generateRegistrationNumber() {
        int year = Year.now().getValue();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return "EXAM-" + year + "-" + sb.toString();
    }
}

