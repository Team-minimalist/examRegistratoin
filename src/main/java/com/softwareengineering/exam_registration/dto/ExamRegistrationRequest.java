package com.softwareengineering.exam_registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamRegistrationRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Exam ID is required")
    private String examId;

    @NotBlank(message = "Status is required")
    private String status;
}