package com.softwareengineering.exam_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String id;
    private String username;
    private String email;
    private String name;
    private String role;
    private boolean profileCompleted;
    private String redirectUrl;
}

