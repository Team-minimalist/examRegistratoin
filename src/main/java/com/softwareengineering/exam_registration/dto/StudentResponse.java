package com.softwareengineering.exam_registration.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.softwareengineering.exam_registration.model.AcademicDetails;
import com.softwareengineering.exam_registration.model.AuthProvider;
import com.softwareengineering.exam_registration.model.IdentificationType;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private String id;
    private String username;
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private IdentificationType identificationType;
    private String identificationNumber;
    private String contactNumber;
    private AcademicDetails academicDetails;
    private UserAddress address;
    private AuthProvider authProvider;
    private Set<Role> roles;
    private boolean active;
    private boolean profileCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
