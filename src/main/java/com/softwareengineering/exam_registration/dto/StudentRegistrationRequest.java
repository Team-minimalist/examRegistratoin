package com.softwareengineering.exam_registration.dto;

import com.softwareengineering.exam_registration.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentRegistrationRequest {

    @Valid
    @NotNull(message = "Personal information is required")
    private PersonalInfo personalInfo;

    @Valid
    @NotNull(message = "Education information is required")
    private Education education;

    @Valid
    @NotNull(message = "Identification information is required")
    private Identification identification;

    @Valid
    @NotNull(message = "Contact information is required")
    private ContactInfo contact;

    @Valid
    @NotNull(message = "Address information is required")
    private Address address;
}