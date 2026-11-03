package com.softwareengineering.exam_registration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.AdminRegistrationRequest;
import com.softwareengineering.exam_registration.dto.AdminResponse;
import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.LoginRequest;
import com.softwareengineering.exam_registration.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AdminAuthController(AdminService adminService,
                               AuthenticationManager authenticationManager) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminResponse>> register(
            @Valid @RequestBody AdminRegistrationRequest request) {

        AdminResponse created = adminService.registerAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AdminResponse>builder()
                        .success(true)
                        .message("Administrator account created successfully. You can now log in.")
                        .data(created)
                        .path("/api/admin/auth/register")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail().trim(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));

        if (!isAdmin) {
            throw new BadCredentialsException("Access denied: Not an administrator account.");
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        AdminResponse adminResponse = adminService.getAdminProfile(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.<AdminResponse>builder()
                        .success(true)
                        .message("Administrator authenticated successfully.")
                        .data(adminResponse)
                        .path("/api/admin/auth/login")
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logged out successfully.")
                        .path("/api/admin/auth/logout")
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminResponse>> getCurrentAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<AdminResponse>builder()
                            .success(false)
                            .message("Not authenticated")
                            .path("/api/admin/auth/me")
                            .build()
            );
        }

        AdminResponse admin = adminService.getAdminProfile(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<AdminResponse>builder()
                        .success(true)
                        .message("Current administrator profile retrieved")
                        .data(admin)
                        .path("/api/admin/auth/me")
                        .build()
        );
    }
}

