package com.softwareengineering.exam_registration.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Username is required")
    private String username;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @JsonIgnore
    private String password;

    @NotBlank(message = "Full name is required")
    private String name;

    private LocalDate dateOfBirth;

    private IdentificationType identificationType;

    private String identificationNumber;

    private String contactNumber;

    @Valid
    private AcademicDetails academicDetails;

    @Valid
    private UserAddress address;

    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    private String googleId;

    @Builder.Default
    private Set<Role> roles = new HashSet<>(Collections.singletonList(Role.ROLE_STUDENT));

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private boolean profileCompleted = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
