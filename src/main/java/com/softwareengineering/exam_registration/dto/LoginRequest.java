package com.softwareengineering.exam_registration.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    @JsonAlias({"usernameOrEmail", "email"})
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public String getUsernameOrEmail() {
        return username;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.username = usernameOrEmail;
    }
}
