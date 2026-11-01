package com.softwareengineering.exam_registration.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.softwareengineering.exam_registration.model.RegistrationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamRegistrationResponse {
    private String id;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String examId;
    private String examName;
    private String conductingOrganization;
    private LocalDate examDate;
    private String registrationNumber;
    private LocalDateTime registeredAt;
    private RegistrationStatus status;
}

