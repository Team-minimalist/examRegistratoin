package com.softwareengineering.exam_registration.util;

import java.time.LocalDate;

import com.softwareengineering.exam_registration.dto.AdminResponse;
import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.model.Admin;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.model.Student;

public class StudentUtil {

    private StudentUtil() {
    }

    public static String calculateExamStatus(Exam exam) {
        if (exam == null || !exam.isActive() || !exam.isPublished()) {
            return "INACTIVE";
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(exam.getRegistrationStartDate())) {
            return "NOT_STARTED";
        }
        if (!today.isBefore(exam.getRegistrationStartDate()) && !today.isAfter(exam.getRegistrationEndDate())) {
            return "OPEN";
        }
        if (today.isAfter(exam.getRegistrationEndDate()) && !today.isAfter(exam.getExaminationDate())) {
            return "CLOSED";
        }
        if (today.isAfter(exam.getExaminationDate())) {
            return "COMPLETED";
        }
        return "CLOSED";
    }

    public static boolean canRegisterForExam(Exam exam) {
        return "OPEN".equalsIgnoreCase(calculateExamStatus(exam));
    }

    public static StudentResponse toStudentResponse(Student student) {
        if (student == null) {
            return null;
        }
        return StudentResponse.builder()
                .id(student.getId())
                .username(student.getUsername())
                .email(student.getEmail())
                .name(student.getName())
                .dateOfBirth(student.getDateOfBirth())
                .identificationType(student.getIdentificationType())
                .identificationNumber(student.getIdentificationNumber())
                .contactNumber(student.getContactNumber())
                .academicDetails(student.getAcademicDetails())
                .address(student.getAddress())
                .authProvider(student.getAuthProvider())
                .roles(student.getRoles())
                .active(student.isActive())
                .profileCompleted(student.isProfileCompleted())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    public static AdminResponse toAdminResponse(Admin admin) {
        if (admin == null) {
            return null;
        }
        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .department(admin.getDepartment())
                .designation(admin.getDesignation())
                .contactNumber(admin.getContactNumber())
                .role(admin.getRole())
                .active(admin.isActive())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

    public static ExamResponse toExamResponse(Exam exam) {
        return toExamResponse(exam, null);
    }

    public static ExamResponse toExamResponse(Exam exam, Long currentRegistrations) {
        if (exam == null) {
            return null;
        }
        String status = calculateExamStatus(exam);
        return ExamResponse.builder()
                .id(exam.getId())
                .examName(exam.getExamName())
                .examCode(exam.getExamCode())
                .description(exam.getDescription())
                .conductingOrganization(exam.getConductingOrganization())
                .examinationType(exam.getExaminationType())
                .examinationDate(exam.getExaminationDate())
                .registrationStartDate(exam.getRegistrationStartDate())
                .registrationEndDate(exam.getRegistrationEndDate())
                .applicationFee(exam.getApplicationFee())
                .eligibilityCriteria(exam.getEligibilityCriteria())
                .maximumCandidates(exam.getMaximumCandidates())
                .currentRegistrations(currentRegistrations)
                .examinationLocation(exam.getExaminationLocation())
                .active(exam.isActive())
                .published(exam.isPublished())
                .registrationStatus(status)
                .canRegister("OPEN".equalsIgnoreCase(status))
                .createdAt(exam.getCreatedAt())
                .updatedAt(exam.getUpdatedAt())
                .build();
    }

    public static ExamRegistrationResponse toRegistrationResponse(ExamRegistration reg, Student student, Exam exam) {
        if (reg == null) {
            return null;
        }
        return ExamRegistrationResponse.builder()
                .id(reg.getId())
                .studentId(reg.getStudentId())
                .studentName(student != null ? student.getName() : "Unknown Candidate")
                .studentEmail(student != null ? student.getEmail() : "Unknown Email")
                .examId(reg.getExamId())
                .examName(exam != null ? exam.getExamName() : "Unknown Examination")
                .examCode(exam != null ? exam.getExamCode() : "")
                .conductingOrganization(exam != null ? exam.getConductingOrganization() : "")
                .examinationDate(exam != null ? exam.getExaminationDate() : null)
                .registrationNumber(reg.getRegistrationNumber())
                .registeredAt(reg.getRegisteredAt())
                .status(reg.getStatus())
                .paymentStatus(reg.getPaymentStatus())
                .updatedAt(reg.getUpdatedAt())
                .build();
    }
}
