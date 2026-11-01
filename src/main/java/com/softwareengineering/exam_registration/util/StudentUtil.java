package com.softwareengineering.exam_registration.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Year;

import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.model.Users;

public class StudentUtil {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private StudentUtil() {
        // Utility class
    }

    /**
     * Generates a unique, student-safe registration number.
     * Example format: EXAM-2026-XXXXXXXX
     */
    public static String generateRegistrationNumber() {
        int year = Year.now().getValue();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return "EXAM-" + year + "-" + sb.toString();
    }

    /**
     * Computes the dynamic registration status of an examination.
     * Possible values: NOT_STARTED, OPEN, CLOSED, COMPLETED, INACTIVE
     */
    public static String calculateExamStatus(Exam exam) {
        if (exam == null || !exam.isActive()) {
            return "INACTIVE";
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(exam.getRegistrationStartDate())) {
            return "NOT_STARTED";
        }
        if (!today.isBefore(exam.getRegistrationStartDate()) && !today.isAfter(exam.getRegistrationEndDate())) {
            return "OPEN";
        }
        if (today.isAfter(exam.getRegistrationEndDate()) && !today.isAfter(exam.getExamDate())) {
            return "CLOSED";
        }
        if (today.isAfter(exam.getExamDate())) {
            return "COMPLETED";
        }
        return "CLOSED";
    }

    public static boolean canRegisterForExam(Exam exam) {
        return "OPEN".equalsIgnoreCase(calculateExamStatus(exam));
    }

    public static StudentResponse toStudentResponse(Users user) {
        if (user == null) {
            return null;
        }
        return StudentResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .identificationType(user.getIdentificationType())
                .identificationNumber(user.getIdentificationNumber())
                .academicDetails(user.getAcademicDetails())
                .address(user.getAddress())
                .authProvider(user.getAuthProvider())
                .role(user.getRole())
                .active(user.isActive())
                .profileCompleted(user.isProfileCompleted())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static ExamResponse toExamResponse(Exam exam) {
        if (exam == null) {
            return null;
        }
        String status = calculateExamStatus(exam);
        return ExamResponse.builder()
                .id(exam.getId())
                .examName(exam.getExamName())
                .description(exam.getDescription())
                .conductingOrganization(exam.getConductingOrganization())
                .examDate(exam.getExamDate())
                .registrationStartDate(exam.getRegistrationStartDate())
                .registrationEndDate(exam.getRegistrationEndDate())
                .active(exam.isActive())
                .registrationStatus(status)
                .canRegister("OPEN".equalsIgnoreCase(status))
                .createdAt(exam.getCreatedAt())
                .updatedAt(exam.getUpdatedAt())
                .build();
    }

    public static ExamRegistrationResponse toRegistrationResponse(ExamRegistration reg, Users student, Exam exam) {
        if (reg == null) {
            return null;
        }
        return ExamRegistrationResponse.builder()
                .id(reg.getId())
                .studentId(reg.getStudentId())
                .studentName(student != null ? student.getName() : "Unknown")
                .studentEmail(student != null ? student.getEmail() : "Unknown")
                .examId(reg.getExamId())
                .examName(exam != null ? exam.getExamName() : "Unknown Exam")
                .conductingOrganization(exam != null ? exam.getConductingOrganization() : "")
                .examDate(exam != null ? exam.getExamDate() : null)
                .registrationNumber(reg.getRegistrationNumber())
                .registeredAt(reg.getRegisteredAt())
                .status(reg.getStatus())
                .build();
    }
}

