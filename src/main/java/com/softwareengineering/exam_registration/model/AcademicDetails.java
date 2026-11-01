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
    private String institutionType; // SCHOOL, COLLEGE, UNIVERSITY

    @NotBlank(message = "Institution name is required")
    private String institutionName;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    // For SCHOOL
    private String className;

    // For SCHOOL, COLLEGE, UNIVERSITY
    private String stream;

    // For COLLEGE, UNIVERSITY
    private String department;
}
