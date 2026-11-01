package com.softwareengineering.exam_registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamRegistrationRepository examRegistrationRepository;

    private ExamService examService;

    @BeforeEach
    void setUp() {
        examService = new ExamServiceImp(examRepository, examRegistrationRepository);
    }

    @Test
    void testCreateExamSuccess() {
        ExamRequest request = ExamRequest.builder()
                .examName("Engineering Entrance 2026")
                .description("National Entrance Test")
                .conductingOrganization("National Testing Board")
                .registrationStartDate(LocalDate.now().minusDays(5))
                .registrationEndDate(LocalDate.now().plusDays(10))
                .examDate(LocalDate.now().plusDays(20))
                .active(true)
                .build();

        Exam savedExam = Exam.builder()
                .id("exam-123")
                .examName(request.getExamName())
                .conductingOrganization(request.getConductingOrganization())
                .registrationStartDate(request.getRegistrationStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .examDate(request.getExamDate())
                .active(true)
                .build();

        when(examRepository.save(any(Exam.class))).thenReturn(savedExam);

        ExamResponse response = examService.createExam(request);

        assertNotNull(response);
        assertEquals("exam-123", response.getId());
        assertEquals("OPEN", response.getRegistrationStatus());
    }

    @Test
    void testInvalidDatesThrowsException() {
        ExamRequest invalidDates = ExamRequest.builder()
                .examName("Invalid Exam")
                .conductingOrganization("Board")
                .registrationStartDate(LocalDate.now().plusDays(10))
                .registrationEndDate(LocalDate.now().plusDays(5)) // end before start!
                .examDate(LocalDate.now().plusDays(20))
                .build();

        assertThrows(InvalidRegistrationException.class, () -> examService.createExam(invalidDates));
    }

    @Test
    void testRegistrationEndAfterExamDateThrowsException() {
        ExamRequest invalidDates = ExamRequest.builder()
                .examName("Invalid Exam")
                .conductingOrganization("Board")
                .registrationStartDate(LocalDate.now().minusDays(5))
                .registrationEndDate(LocalDate.now().plusDays(15))
                .examDate(LocalDate.now().plusDays(10)) // exam before reg end!
                .build();

        assertThrows(InvalidRegistrationException.class, () -> examService.createExam(invalidDates));
    }
}

