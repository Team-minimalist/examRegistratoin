package com.softwareengineering.exam_registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.softwareengineering.exam_registration.dto.AdminRegistrationRequest;
import com.softwareengineering.exam_registration.dto.AdminResponse;
import com.softwareengineering.exam_registration.dto.AdminStatsResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.model.Admin;
import com.softwareengineering.exam_registration.model.RegistrationStatus;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.repository.AdminRepository;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamRegistrationRepository examRegistrationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        AdminServiceImp imp = new AdminServiceImp(
                adminRepository, studentRepository, examRepository, examRegistrationRepository, passwordEncoder);
        ReflectionTestUtils.setField(imp, "expectedInvitationCode", "ADMIN-SECRET-2026");
        adminService = imp;
    }

    @Test
    void testRegisterFirstAdminSuccessWithoutInvitationCode() {
        AdminRegistrationRequest request = AdminRegistrationRequest.builder()
                .name("Super Admin")
                .username("superadmin")
                .email("admin@portal.gov.in")
                .password("Admin@1234")
                .confirmPassword("Admin@1234")
                .department("Examinations")
                .designation("Controller")
                .build();

        when(adminRepository.count()).thenReturn(0L);
        when(adminRepository.existsByUsername("superadmin")).thenReturn(false);
        when(adminRepository.existsByEmail("admin@portal.gov.in")).thenReturn(false);
        when(studentRepository.existsByUsername("superadmin")).thenReturn(false);
        when(studentRepository.existsByEmail("admin@portal.gov.in")).thenReturn(false);
        when(passwordEncoder.encode("Admin@1234")).thenReturn("encodedSecret");

        Admin saved = Admin.builder()
                .id("adm-1")
                .name("Super Admin")
                .username("superadmin")
                .email("admin@portal.gov.in")
                .role(Role.ROLE_ADMIN)
                .active(true)
                .build();

        when(adminRepository.save(any(Admin.class))).thenReturn(saved);

        AdminResponse response = adminService.registerAdmin(request);

        assertNotNull(response);
        assertEquals("superadmin", response.getUsername());
        assertEquals("admin@portal.gov.in", response.getEmail());
        assertEquals(Role.ROLE_ADMIN, response.getRole());
    }

    @Test
    void testRegisterSubsequentAdminRequiresValidInvitationCode() {
        AdminRegistrationRequest request = AdminRegistrationRequest.builder()
                .name("Second Admin")
                .username("secondadmin")
                .email("second@portal.gov.in")
                .password("Admin@1234")
                .confirmPassword("Admin@1234")
                .invitationCode("WRONG-CODE")
                .build();

        when(adminRepository.count()).thenReturn(1L);

        assertThrows(InvalidRegistrationException.class, () -> adminService.registerAdmin(request));
    }

    @Test
    void testRegisterAdminPasswordMismatch() {
        AdminRegistrationRequest request = AdminRegistrationRequest.builder()
                .name("Admin")
                .username("admin1")
                .email("admin1@portal.gov.in")
                .password("Admin@1234")
                .confirmPassword("Mismatch@1234")
                .build();

        assertThrows(InvalidRegistrationException.class, () -> adminService.registerAdmin(request));
    }

    @Test
    void testGetDashboardStats() {
        when(studentRepository.count()).thenReturn(100L);
        when(studentRepository.countByActiveTrue()).thenReturn(90L);
        when(examRepository.count()).thenReturn(10L);
        when(examRepository.countByActiveTrue()).thenReturn(8L);
        when(examRepository.countByPublishedTrue()).thenReturn(7L);
        when(examRegistrationRepository.count()).thenReturn(250L);
        when(examRegistrationRepository.countByStatus(RegistrationStatus.REGISTERED)).thenReturn(240L);
        when(examRegistrationRepository.countByStatus(RegistrationStatus.CANCELLED)).thenReturn(10L);

        AdminStatsResponse stats = adminService.getDashboardStats();

        assertNotNull(stats);
        assertEquals(100L, stats.getTotalStudents());
        assertEquals(90L, stats.getActiveStudents());
        assertEquals(10L, stats.getTotalExams());
        assertEquals(250L, stats.getTotalRegistrations());
    }
}

