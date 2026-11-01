package com.softwareengineering.exam_registration.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "exams")
public class Exam {

    @Id
    private String id;

    @NotBlank(message = "Exam name is required")
    private String examName;

    @Indexed(unique = true)
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
    private boolean active = true;

    @Builder.Default
    private boolean published = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
