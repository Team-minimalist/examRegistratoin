package com.softwareengineering.exam_registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.model.RegistrationStatus;
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ExamRegistrationServiceTest {

    @Mock
    private ExamRegistrationRepository examRegistrationRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private UserRepository userRepository;

    private ExamRegistrationService examRegistrationService;

    @BeforeEach
    void setUp() {
        examRegistrationService = new ExamRegistrationServiceImp(
                examRegistrationRepository, examRepository, userRepository);
    }

    @Test
    void testRegisterProfileIncompleteFails() {
        Users incompleteStudent = Users.builder()
                .id("std-1")
                .username("student1")
                .profileCompleted(false)
                .build();

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(incompleteStudent));

        assertThrows(InvalidRegistrationException.class, () ->
                examRegistrationService.registerForExam("exam-1", "student1"));
    }

    @Test
    void testRegisterClosedExamFails() {
        Users completedStudent = Users.builder()
                .id("std-1")
                .username("student1")
                .profileCompleted(true)
                .build();

        Exam closedExam = Exam.builder()
                .id("exam-1")
                .examName("Closed Exam")
                .conductingOrganization("Board")
                .registrationStartDate(LocalDate.now().minusDays(20))
                .registrationEndDate(LocalDate.now().minusDays(5))
                .examDate(LocalDate.now().plusDays(5))
                .active(true)
                .build();

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(completedStudent));
        when(examRepository.findById("exam-1")).thenReturn(Optional.of(closedExam));

        assertThrows(InvalidRegistrationException.class, () ->
                examRegistrationService.registerForExam("exam-1", "student1"));
    }

    @Test
    void testRegisterDuplicateFails() {
        Users completedStudent = Users.builder()
                .id("std-1")
                .username("student1")
                .profileCompleted(true)
                .build();

        Exam openExam = Exam.builder()
                .id("exam-1")
                .examName("Open Exam")
                .conductingOrganization("Board")
                .registrationStartDate(LocalDate.now().minusDays(2))
                .registrationEndDate(LocalDate.now().plusDays(10))
                .examDate(LocalDate.now().plusDays(20))
                .active(true)
                .build();

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(completedStudent));
        when(examRepository.findById("exam-1")).thenReturn(Optional.of(openExam));
        when(examRegistrationRepository.existsByStudentIdAndExamId("std-1", "exam-1")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () ->
                examRegistrationService.registerForExam("exam-1", "student1"));
    }

    @Test
    void testRegisterSuccess() {
        Users completedStudent = Users.builder()
                .id("std-1")
                .username("student1")
                .name("Student One")
                .email("student1@example.com")
                .profileCompleted(true)
                .build();

        Exam openExam = Exam.builder()
                .id("exam-1")
                .examName("Open Exam")
                .conductingOrganization("Board")
                .registrationStartDate(LocalDate.now().minusDays(2))
                .registrationEndDate(LocalDate.now().plusDays(10))
                .examDate(LocalDate.now().plusDays(20))
                .active(true)
                .build();

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(completedStudent));
        when(examRepository.findById("exam-1")).thenReturn(Optional.of(openExam));
        when(examRegistrationRepository.existsByStudentIdAndExamId("std-1", "exam-1")).thenReturn(false);

        when(examRegistrationRepository.save(any(ExamRegistration.class))).thenAnswer(invocation -> {
            ExamRegistration r = invocation.getArgument(0);
            r.setId("reg-999");
            return r;
        });

        ExamRegistrationResponse response = examRegistrationService.registerForExam("exam-1", "student1");

        assertNotNull(response);
        assertEquals("std-1", response.getStudentId());
        assertEquals("exam-1", response.getExamId());
        assertTrue(response.getRegistrationNumber().startsWith("EXAM-"));
        assertEquals(RegistrationStatus.REGISTERED, response.getStatus());
    }
}

