package com.softwareengineering.exam_registration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.StudentProfileUpdateRequest;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private String resolveUsernameOrEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            if (email != null) {
                return email;
            }
        }
        return authentication.getName();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> getMyProfile(Authentication authentication) {
        String usernameOrEmail = resolveUsernameOrEmail(authentication);
        StudentResponse response = studentService.getStudentProfile(usernameOrEmail);

        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Student profile retrieved")
                        .data(response)
                        .path("/api/students/me")
                        .build()
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody StudentProfileUpdateRequest request) {

        String usernameOrEmail = resolveUsernameOrEmail(authentication);
        StudentResponse updated = studentService.updateStudentProfile(usernameOrEmail, request);

        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Profile updated successfully")
                        .data(updated)
                        .path("/api/students/me")
                        .build()
        );
    }
}
