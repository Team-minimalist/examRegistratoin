package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.exception.ResourceNotFoundException;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.util.StudentUtil;

@Service
public class ExamServiceImp implements ExamService {

    private final ExamRepository examRepository;
    private final ExamRegistrationRepository examRegistrationRepository;

    public ExamServiceImp(ExamRepository examRepository,
                          ExamRegistrationRepository examRegistrationRepository) {
        this.examRepository = examRepository;
        this.examRegistrationRepository = examRegistrationRepository;
    }

    private void validateExamDates(ExamRequest request) {
        if (request.getRegistrationStartDate() == null || request.getRegistrationEndDate() == null || request.getExamDate() == null) {
            throw new InvalidRegistrationException("Registration start date, end date, and exam date are all required");
        }
        if (!request.getRegistrationStartDate().isBefore(request.getRegistrationEndDate())) {
            throw new InvalidRegistrationException("Registration start date must be strictly before registration end date");
        }
        if (request.getRegistrationEndDate().isAfter(request.getExamDate())) {
            throw new InvalidRegistrationException("Registration end date cannot be after the exam date");
        }
    }

    @Override
    public List<ExamResponse> getAvailableExams() {
        return examRepository.findByActiveTrue().stream()
                .map(StudentUtil::toExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponse> getAllExamsForAdmin() {
        return examRepository.findAll().stream()
                .map(StudentUtil::toExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(String id) {
        Exam exam = getExamEntityById(id);
        return StudentUtil.toExamResponse(exam);
    }

    @Override
    public Exam getExamEntityById(String id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examination not found with ID: " + id));
    }

    @Override
    public ExamResponse createExam(ExamRequest request) {
        validateExamDates(request);

        Exam exam = Exam.builder()
                .examName(request.getExamName().trim())
                .description(request.getDescription())
                .conductingOrganization(request.getConductingOrganization().trim())
                .examDate(request.getExamDate())
                .registrationStartDate(request.getRegistrationStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .active(request.getActive() != null ? request.getActive() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Exam saved = examRepository.save(exam);
        return StudentUtil.toExamResponse(saved);
    }

    @Override
    public ExamResponse updateExam(String id, ExamRequest request) {
        validateExamDates(request);

        Exam exam = getExamEntityById(id);

        exam.setExamName(request.getExamName().trim());
        exam.setDescription(request.getDescription());
        exam.setConductingOrganization(request.getConductingOrganization().trim());
        exam.setExamDate(request.getExamDate());
        exam.setRegistrationStartDate(request.getRegistrationStartDate());
        exam.setRegistrationEndDate(request.getRegistrationEndDate());
        if (request.getActive() != null) {
            exam.setActive(request.getActive());
        }
        exam.setUpdatedAt(LocalDateTime.now());

        Exam saved = examRepository.save(exam);
        return StudentUtil.toExamResponse(saved);
    }

    @Override
    public ExamResponse toggleExamStatus(String id) {
        Exam exam = getExamEntityById(id);
        exam.setActive(!exam.isActive());
        exam.setUpdatedAt(LocalDateTime.now());
        Exam saved = examRepository.save(exam);
        return StudentUtil.toExamResponse(saved);
    }

    @Override
    public void deleteExam(String id) {
        Exam exam = getExamEntityById(id);
        if (!examRegistrationRepository.findByExamId(id).isEmpty()) {
            throw new InvalidRegistrationException(
                    "Cannot delete examination that already has registered students. You can deactivate it instead.");
        }
        examRepository.delete(exam);
    }
}

