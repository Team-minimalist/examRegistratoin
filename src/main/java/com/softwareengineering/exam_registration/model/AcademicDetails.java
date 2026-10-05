package com.softwareengineering.exam_registration.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicDetails {

    @NotBlank(message = "Institution type is required")
    private String institutionType;

    @NotBlank(message = "Institution name is required")
    private String institutionName;

    private String className;

    private String stream;

    private String department;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;
}