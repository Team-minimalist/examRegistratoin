package com.softwareengineering.exam_registration.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicDetails {

    @NotBlank(message = "Institution type is required")
    private String institutionType;

    @NotBlank(message = "Institution name is required")
    private String institutionName;

    @NotBlank(message = "Class name is required")
    private String className;

    private String stream;

    private String department;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;
}

