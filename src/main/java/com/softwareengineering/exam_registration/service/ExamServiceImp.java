package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
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
        if (request.getRegistrationStartDate() == null || request.getRegistrationEndDate() == null || request.getExaminationDate() == null) {
            throw new InvalidRegistrationException("Registration start date, end date, and examination date are all required");
        }
        if (!request.getRegistrationStartDate().isBefore(request.getRegistrationEndDate())) {
            throw new InvalidRegistrationException("Registration start date must be strictly before registration end date");
        }
        if (request.getRegistrationEndDate().isAfter(request.getExaminationDate())) {
            throw new InvalidRegistrationException("Registration end date cannot be after the examination date");
        }
    }

    private String generateUniqueExamCode(String examName) {
        String prefix = examName.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        if (prefix.length() > 4) {
            prefix = prefix.substring(0, 4);
        } else if (prefix.isEmpty()) {
            prefix = "EXAM";
        }
        String code;
        do {
            code = prefix + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        } while (examRepository.existsByExamCode(code));
        return code;
    }

    @Override
    public List<ExamResponse> getAvailableExams() {
        return examRepository.findByActiveTrueAndPublishedTrue().stream()
                .map(exam -> {
                    long count = examRegistrationRepository.countByExamId(exam.getId());
                    return StudentUtil.toExamResponse(exam, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponse> getAllExamsForAdmin() {
        return examRepository.findAll().stream()
                .map(exam -> {
                    long count = examRegistrationRepository.countByExamId(exam.getId());
                    return StudentUtil.toExamResponse(exam, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(String id) {
        Exam exam = getExamEntityById(id);
        long count = examRegistrationRepository.countByExamId(exam.getId());
        return StudentUtil.toExamResponse(exam, count);
    }

    @Override
    public Exam getExamEntityById(String id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examination not found with ID: " + id));
    }

    @Override
    public ExamResponse createExam(ExamRequest request) {
        validateExamDates(request);

        String examCode = request.getExamCode();
        if (examCode != null && !examCode.trim().isEmpty()) {
            examCode = examCode.trim().toUpperCase();
            if (examRepository.existsByExamCode(examCode)) {
                throw new DuplicateResourceException("Examination code '" + examCode + "' is already in use");
            }
        } else {
            examCode = generateUniqueExamCode(request.getExamName());
        }

        Exam exam = Exam.builder()
                .examName(request.getExamName().trim())
                .examCode(examCode)
                .description(request.getDescription())
                .conductingOrganization(request.getConductingOrganization().trim())
                .examinationType(request.getExaminationType())
                .examinationDate(request.getExaminationDate())
                .registrationStartDate(request.getRegistrationStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .applicationFee(request.getApplicationFee())
                .eligibilityCriteria(request.getEligibilityCriteria())
                .maximumCandidates(request.getMaximumCandidates())
                .examinationLocation(request.getExaminationLocation())
                .active(request.getActive() != null ? request.getActive() : true)
                .published(request.getPublished() != null ? request.getPublished() : true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Exam saved = examRepository.save(exam);
        return StudentUtil.toExamResponse(saved, 0L);
    }

    @Override
    public ExamResponse updateExam(String id, ExamRequest request) {
        validateExamDates(request);

        Exam exam = getExamEntityById(id);

        if (request.getExamCode() != null && !request.getExamCode().trim().isEmpty()) {
            String newCode = request.getExamCode().trim().toUpperCase();
            if (!newCode.equals(exam.getExamCode()) && examRepository.existsByExamCode(newCode)) {
                throw new DuplicateResourceException("Examination code '" + newCode + "' is already in use");
            }
            exam.setExamCode(newCode);
        }

        exam.setExamName(request.getExamName().trim());
        exam.setDescription(request.getDescription());
        exam.setConductingOrganization(request.getConductingOrganization().trim());
        exam.setExaminationType(request.getExaminationType());
        exam.setExaminationDate(request.getExaminationDate());
        exam.setRegistrationStartDate(request.getRegistrationStartDate());
        exam.setRegistrationEndDate(request.getRegistrationEndDate());
        exam.setApplicationFee(request.getApplicationFee());
        exam.setEligibilityCriteria(request.getEligibilityCriteria());
        exam.setMaximumCandidates(request.getMaximumCandidates());
        exam.setExaminationLocation(request.getExaminationLocation());
        if (request.getActive() != null) {
            exam.setActive(request.getActive());
        }
        if (request.getPublished() != null) {
            exam.setPublished(request.getPublished());
        }
        exam.setUpdatedAt(LocalDateTime.now());

        Exam saved = examRepository.save(exam);
        long count = examRegistrationRepository.countByExamId(saved.getId());
        return StudentUtil.toExamResponse(saved, count);
    }

    @Override
    public ExamResponse toggleExamStatus(String id) {
        Exam exam = getExamEntityById(id);
        exam.setActive(!exam.isActive());
        exam.setUpdatedAt(LocalDateTime.now());
        Exam saved = examRepository.save(exam);
        long count = examRegistrationRepository.countByExamId(saved.getId());
        return StudentUtil.toExamResponse(saved, count);
    }

    @Override
    public ExamResponse toggleExamPublishStatus(String id) {
        Exam exam = getExamEntityById(id);
        exam.setPublished(!exam.isPublished());
        exam.setUpdatedAt(LocalDateTime.now());
        Exam saved = examRepository.save(exam);
        long count = examRegistrationRepository.countByExamId(saved.getId());
        return StudentUtil.toExamResponse(saved, count);
    }

    @Override
    public void deleteExam(String id) {
        Exam exam = getExamEntityById(id);
        if (!examRegistrationRepository.findByExamId(id).isEmpty()) {
            throw new InvalidRegistrationException(
                    "Cannot delete examination that already has registered candidates. You can deactivate or unpublish it instead.");
        }
        examRepository.delete(exam);
    }
}
