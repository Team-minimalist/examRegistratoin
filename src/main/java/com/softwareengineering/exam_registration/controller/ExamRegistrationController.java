package com.softwareengineering.exam_registration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.service.ExamRegistrationService;

@RestController
@RequestMapping
public class ExamRegistrationController {

    private final ExamRegistrationService examRegistrationService;

    public ExamRegistrationController(ExamRegistrationService examRegistrationService) {
        this.examRegistrationService = examRegistrationService;
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

    private boolean checkIsAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ADMIN"));
    }

    @PostMapping("/api/exams/{examId}/register")
    public ResponseEntity<ApiResponse<ExamRegistrationResponse>> registerForExam(
            @PathVariable String examId,
            Authentication authentication) {

        String usernameOrEmail = resolveUsernameOrEmail(authentication);
        ExamRegistrationResponse response = examRegistrationService.registerForExam(examId, usernameOrEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ExamRegistrationResponse>builder()
                        .success(true)
                        .message("Successfully registered for examination")
                        .data(response)
                        .path("/api/exams/" + examId + "/register")
                        .build()
        );
    }

    @GetMapping("/api/registrations/me")
    public ResponseEntity<ApiResponse<List<ExamRegistrationResponse>>> getMyRegistrations(
            Authentication authentication) {

        String usernameOrEmail = resolveUsernameOrEmail(authentication);
        List<ExamRegistrationResponse> registrations = examRegistrationService.getMyRegistrations(usernameOrEmail);

        return ResponseEntity.ok(
                ApiResponse.<List<ExamRegistrationResponse>>builder()
                        .success(true)
                        .message("My registrations retrieved")
                        .data(registrations)
                        .path("/api/registrations/me")
                        .build()
        );
    }

    @GetMapping("/api/registrations/{id}")
    public ResponseEntity<ApiResponse<ExamRegistrationResponse>> getRegistrationById(
            @PathVariable String id,
            Authentication authentication) {

        String usernameOrEmail = resolveUsernameOrEmail(authentication);
        boolean isAdmin = checkIsAdmin(authentication);
        ExamRegistrationResponse response = examRegistrationService.getRegistrationById(id, usernameOrEmail, isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<ExamRegistrationResponse>builder()
                        .success(true)
                        .message("Registration details retrieved")
                        .data(response)
                        .path("/api/registrations/" + id)
                        .build()
        );
    }

    @PatchMapping("/api/registrations/{id}/cancel")
    public ResponseEntity<ApiResponse<ExamRegistrationResponse>> cancelRegistration(
            @PathVariable String id,
            Authentication authentication) {

        String usernameOrEmail = resolveUsernameOrEmail(authentication);
        boolean isAdmin = checkIsAdmin(authentication);
        ExamRegistrationResponse response = examRegistrationService.cancelRegistration(id, usernameOrEmail, isAdmin);

        return ResponseEntity.ok(
                ApiResponse.<ExamRegistrationResponse>builder()
                        .success(true)
                        .message("Registration cancelled successfully")
                        .data(response)
                        .path("/api/registrations/" + id + "/cancel")
                        .build()
        );
    }
}
