package com.softwareengineering.exam_registration.dto;

import com.softwareengineering.exam_registration.model.StudentAcademicDetails;
import com.softwareengineering.exam_registration.model.StudentAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class StudentRegistrationRequest {
        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid Email Format")
        private String email;

        @NotBlank(message = "Phone name is required")
        private String phone;

        @Valid //also validate the field inside the structure
        private StudentAddress studentAddress;

        @Valid 
        private StudentAcademicDetails studentAcademicDetails;



}
