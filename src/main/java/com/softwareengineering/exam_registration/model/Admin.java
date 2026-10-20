package com.softwareengineering.exam_registration.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Document(collection = "admins")
public class Admin {
    @Id 
    private String id;

    @NotBlank(message = "Must fill first name.")
    private String adminFirstName;

    @NotBlank(message = "Must fill last name.")
    private String adminLastName;

    @NotBlank(message = "Must fill Email.")
    @Email 
    private String adminEmail;

    @NotBlank(message = "Must fill phone number.")
    private String adminPhoneNo;

    @NotBlank(message = "Must fill  Role.")
    private String  adminRole;

    @Valid 
    private AdminAddress adminAddress;
}
