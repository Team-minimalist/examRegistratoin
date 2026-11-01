package com.softwareengineering.exam_registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;

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
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IdentificationService identificationService;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImp(studentRepository, passwordEncoder, identificationService);
    }

    @Test
    void testRegisterStudentSuccess() {
        StudentRegistrationRequest request = StudentRegistrationRequest.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("Secret@123")
                .name("John Doe")
                .build();

        when(studentRepository.existsByUsername("john_doe")).thenReturn(false);
        when(studentRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret@123")).thenReturn("hashedPassword");

        Student savedStudent = Student.builder()
                .id("s-1")
                .username("john_doe")
                .email("john@example.com")
                .name("John Doe")
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_STUDENT)))
                .authProvider(AuthProvider.LOCAL)
                .active(true)
                .profileCompleted(false)
                .build();

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

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

        when(studentRepository.existsByUsername("existing_user")).thenReturn(true);

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

        when(studentRepository.existsByUsername("new_user")).thenReturn(false);
        when(studentRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.registerStudent(request));
    }
}
