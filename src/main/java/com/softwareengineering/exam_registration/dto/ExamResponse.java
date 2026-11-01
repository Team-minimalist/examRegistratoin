package com.softwareengineering.exam_registration.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private String id;
    private String examName;
    private String description;
    private String conductingOrganization;
    private LocalDate examDate;
    private LocalDate registrationStartDate;
    private LocalDate registrationEndDate;
    private boolean active;
    private String registrationStatus; // NOT_STARTED, OPEN, CLOSED, COMPLETED, INACTIVE
    private boolean canRegister;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

