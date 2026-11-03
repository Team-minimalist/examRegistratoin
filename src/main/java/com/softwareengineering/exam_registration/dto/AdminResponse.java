package com.softwareengineering.exam_registration.dto;

import java.time.LocalDateTime;

import com.softwareengineering.exam_registration.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private String id;
    private String name;
    private String username;
    private String email;
    private String department;
    private String designation;
    private String contactNumber;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

