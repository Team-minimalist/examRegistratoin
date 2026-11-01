package com.softwareengineering.exam_registration.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.service.StudentService;
import com.softwareengineering.exam_registration.util.StudentUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentService studentService;

    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StudentResponse>> register(
            @Valid @RequestBody StudentRegistrationRequest request) {

        StudentResponse created = studentService.registerStudent(request);

        ApiResponse<StudentResponse> response = ApiResponse.<StudentResponse>builder()
                .success(true)
                .message("Student registered successfully! Please log in to complete your profile.")
                .data(created)
                .path("/api/auth/register")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<StudentResponse>builder()
                            .success(false)
                            .message("Not authenticated")
                            .path("/api/auth/me")
                            .build()
            );
        }

        String usernameOrEmail = resolvePrincipalUsernameOrEmail(authentication);
        StudentResponse user = studentService.getStudentProfile(usernameOrEmail);

        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Current user profile retrieved")
                        .data(user)
                        .path("/api/auth/me")
                        .build()
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuthStatus(Authentication authentication) {
        Map<String, Object> data = new HashMap<>();
        boolean authenticated = authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());

        data.put("authenticated", authenticated);

        if (authenticated) {
            String usernameOrEmail = resolvePrincipalUsernameOrEmail(authentication);
            try {
                StudentResponse profile = studentService.getStudentProfile(usernameOrEmail);
                data.put("user", profile);
            } catch (Exception e) {
                data.put("username", usernameOrEmail);
            }
        }

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Object>>builder()
                        .success(true)
                        .data(data)
                        .path("/api/auth/status")
                        .build()
        );
    }

    private String resolvePrincipalUsernameOrEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            if (email != null) {
                return email;
            }
        }
        return authentication.getName();
    }
}
