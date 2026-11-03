package com.softwareengineering.exam_registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamRegistrationRequest {
    @NotBlank(message = "Exam ID is required")
    private String examId;
}

