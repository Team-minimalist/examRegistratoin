package com.softwareengineering.exam_registration.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamRequest {

    @NotBlank(message = "Exam name is required")
    private String examName;

    private String description;

    @NotBlank(message = "Exam type is required")
    private String examType;

    @NotNull(message = "Registration start time is required")
    private LocalDateTime registrationStart;

    @NotNull(message = "Registration end time is required")
    private LocalDateTime registrationEnd;

    @NotNull(message = "Exam start time is required")
    private LocalDateTime examStart;

    @NotNull(message = "Exam end time is required")
    private LocalDateTime examEnd;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @NotNull(message = "Total marks are required")
    @Min(value = 1, message = "Total marks must be at least 1")
    private Integer totalMarks;

    @NotNull(message = "Passing marks are required")
    @Min(value = 1, message = "Passing marks must be at least 1")
    private Integer passingMarks;

    @NotBlank(message = "Status is required")
    private String status;
}