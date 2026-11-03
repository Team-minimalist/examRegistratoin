package com.softwareengineering.exam_registration.dto;

import java.time.LocalDate;

import com.softwareengineering.exam_registration.model.AcademicDetails;
import com.softwareengineering.exam_registration.model.IdentificationType;
import com.softwareengineering.exam_registration.model.UserAddress;

import jakarta.validation.Valid;
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
public class StudentProfileRequest {

    @NotBlank(message = "Full name is required")
    private String name;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Identification type is required")
    private IdentificationType identificationType;

    @NotBlank(message = "Identification number is required")
    private String identificationNumber;

    private String contactNumber;

    @NotNull(message = "Academic details are required")
    @Valid
    private AcademicDetails academicDetails;

    @NotNull(message = "Address details are required")
    @Valid
    private UserAddress address;
}

