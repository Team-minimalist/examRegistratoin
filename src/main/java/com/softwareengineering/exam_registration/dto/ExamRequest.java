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

    private String description;

    @NotBlank(message = "Conducting organization is required")
    private String conductingOrganization;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @NotNull(message = "Registration start date is required")
    private LocalDate registrationStartDate;

    @NotNull(message = "Registration end date is required")
    private LocalDate registrationEndDate;

    @Builder.Default
    private Boolean active = true;
}
