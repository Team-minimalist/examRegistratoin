package com.softwareengineering.exam_registration.dto;

import com.softwareengineering.exam_registration.model.AdminAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequest {

    @NotBlank(message = "First name is required")
    private String adminFirstName;

    @NotBlank(message = "Last name is required")
    private String adminLastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String adminEmail;

    @NotBlank(message = "Phone number is required")
    private String adminPhoneNo;

    @NotBlank(message = "Role is required")
    private String adminRole;

    @Valid
    private AdminAddress adminAddress;
}