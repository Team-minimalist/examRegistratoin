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
    private String examCode;
    private String description;
    private String conductingOrganization;
    private String examinationType;
    private LocalDate examinationDate;
    private LocalDate registrationStartDate;
    private LocalDate registrationEndDate;
    private Double applicationFee;
    private String eligibilityCriteria;
    private Integer maximumCandidates;
    private Long currentRegistrations;
    private String examinationLocation;
    private boolean active;
    private boolean published;
    private String registrationStatus; // NOT_STARTED, OPEN, CLOSED, COMPLETED, INACTIVE
    private boolean canRegister;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
