package com.softwareengineering.exam_registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
import com.softwareengineering.exam_registration.model.AuthProvider;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IdentificationService identificationService;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImp(userRepository, passwordEncoder, identificationService);
    }

    @Test
    void testRegisterStudentSuccess() {
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("Secret@123")
                .name("John Doe")
                .build();

        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret@123")).thenReturn("hashedPassword");

        Users savedUser = Users.builder()
                .id("u-1")
                .username("john_doe")
                .email("john@example.com")
                .name("John Doe")
                .role(Role.STUDENT)
                .authProvider(AuthProvider.LOCAL)
                .active(true)
                .profileCompleted(false)
                .build();

        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        StudentResponse response = studentService.registerStudent(request);

        assertNotNull(response);
        assertEquals("john_doe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());
        assertFalse(response.isProfileCompleted());
        verify(passwordEncoder).encode("Secret@123");
    }

    @Test
    void testRegisterStudentDuplicateUsername() {
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
                .username("existing_user")
                .email("john@example.com")
                .password("Secret@123")
                .name("John Doe")
                .build();

        when(userRepository.existsByUsername("existing_user")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.registerStudent(request));
    }

    @Test
    void testRegisterStudentDuplicateEmail() {
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
                .username("new_user")
                .email("existing@example.com")
                .password("Secret@123")
                .name("John Doe")
                .build();

        when(userRepository.existsByUsername("new_user")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.registerStudent(request));
    }
}

