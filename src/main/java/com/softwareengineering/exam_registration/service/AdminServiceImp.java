package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.dto.AdminRegistrationRequest;
import com.softwareengineering.exam_registration.dto.AdminResponse;
import com.softwareengineering.exam_registration.dto.AdminStatsResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.exception.ResourceNotFoundException;
import com.softwareengineering.exam_registration.model.Admin;
import com.softwareengineering.exam_registration.model.RegistrationStatus;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.repository.AdminRepository;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;
import com.softwareengineering.exam_registration.util.StudentUtil;

@Service
public class AdminServiceImp implements AdminService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final ExamRegistrationRepository examRegistrationRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.invitation-code:ADMIN-SECRET-2026}")
    private String expectedInvitationCode;

    public AdminServiceImp(AdminRepository adminRepository,
                           StudentRepository studentRepository,
                           ExamRepository examRepository,
                           ExamRegistrationRepository examRegistrationRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.examRegistrationRepository = examRegistrationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminResponse registerAdmin(AdminRegistrationRequest request) {
        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRegistrationException("Passwords do not match");
        }

        long adminCount = adminRepository.count();
        if (adminCount > 0) {
            if (request.getInvitationCode() == null || !expectedInvitationCode.equals(request.getInvitationCode().trim())) {
                throw new InvalidRegistrationException("Invalid or missing administrator invitation code");
            }
        }

        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (adminRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Administrator username '" + username + "' is already taken");
        }
        if (adminRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Administrator email '" + email + "' is already registered");
        }
        if (studentRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username '" + username + "' is already registered to a student account");
        }
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email '" + email + "' is already registered to a student account");
        }

        Admin admin = Admin.builder()
                .name(request.getName().trim())
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .contactNumber(request.getContactNumber())
                .role(Role.ROLE_ADMIN)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Admin saved = adminRepository.save(admin);
        return StudentUtil.toAdminResponse(saved);
    }

    @Override
    public AdminResponse getAdminProfile(String usernameOrEmail) {
        Admin admin = getAdminByUsernameOrEmail(usernameOrEmail);
        return StudentUtil.toAdminResponse(admin);
    }

    @Override
    public Admin getAdminByUsernameOrEmail(String usernameOrEmail) {
        return adminRepository.findByUsername(usernameOrEmail)
                .or(() -> adminRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Administrator not found: " + usernameOrEmail));
    }

    @Override
    public AdminStatsResponse getDashboardStats() {
        return AdminStatsResponse.builder()
                .totalStudents(studentRepository.count())
                .activeStudents(studentRepository.countByActiveTrue())
                .totalExams(examRepository.count())
                .activeExams(examRepository.countByActiveTrue())
                .publishedExams(examRepository.countByPublishedTrue())
                .totalRegistrations(examRegistrationRepository.count())
                .activeRegistrations(examRegistrationRepository.countByStatus(RegistrationStatus.REGISTERED))
                .cancelledRegistrations(examRegistrationRepository.countByStatus(RegistrationStatus.CANCELLED))
                .build();
    }

    @Override
    public void bootstrapInitialAdmin(String email, String password, String name, String username) {
        if (email == null || email.isBlank()) {
            return;
        }
        String cleanEmail = email.trim().toLowerCase();
        String cleanUsername = (username != null && !username.isBlank()) ? username.trim() : cleanEmail.split("@")[0];

        if (!adminRepository.existsByEmail(cleanEmail) && !adminRepository.existsByUsername(cleanUsername)) {
            Admin initialAdmin = Admin.builder()
                    .name(name != null && !name.isBlank() ? name.trim() : "System Administrator")
                    .username(cleanUsername)
                    .email(cleanEmail)
                    .password(passwordEncoder.encode(password))
                    .department("Examinations Directorate")
                    .designation("Lead Administrator")
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            adminRepository.save(initialAdmin);
        }
    }
}
