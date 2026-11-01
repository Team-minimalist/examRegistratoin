package com.softwareengineering.exam_registration.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {

    @NotBlank(message = "Exam name is required")
    private String examName;

    private String examCode;

    private String description;

    @NotBlank(message = "Conducting organization is required")
    private String conductingOrganization;

    private String examinationType;

    @NotNull(message = "Examination date is required")
    private LocalDate examinationDate;

    @NotNull(message = "Registration start date is required")
    private LocalDate registrationStartDate;

    @NotNull(message = "Registration end date is required")
    private LocalDate registrationEndDate;

    private Double applicationFee;

    private String eligibilityCriteria;

    private Integer maximumCandidates;

    private String examinationLocation;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean published = true;
}
