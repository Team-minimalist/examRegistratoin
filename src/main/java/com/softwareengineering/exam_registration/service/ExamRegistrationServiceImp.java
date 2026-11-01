package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
import com.softwareengineering.exam_registration.exception.InvalidRegistrationException;
import com.softwareengineering.exam_registration.exception.ResourceNotFoundException;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.model.RegistrationStatus;
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.UserRepository;
import com.softwareengineering.exam_registration.util.StudentUtil;

@Service
public class ExamRegistrationServiceImp implements ExamRegistrationService {

    private final ExamRegistrationRepository examRegistrationRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    public ExamRegistrationServiceImp(ExamRegistrationRepository examRegistrationRepository,
                                      ExamRepository examRepository,
                                      UserRepository userRepository) {
        this.examRegistrationRepository = examRegistrationRepository;
        this.examRepository = examRepository;
        this.userRepository = userRepository;
    }

    private Users getStudent(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + usernameOrEmail));
    }

    @Override
    public ExamRegistrationResponse registerForExam(String examId, String usernameOrEmail) {
        Users student = getStudent(usernameOrEmail);

        if (!student.isProfileCompleted()) {
            throw new InvalidRegistrationException(
                    "Please complete your student profile before registering for an examination.");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Examination not found with ID: " + examId));

        if (!exam.isActive()) {
            throw new InvalidRegistrationException("Examination is currently inactive.");
        }

        String examStatus = StudentUtil.calculateExamStatus(exam);
        if (!"OPEN".equalsIgnoreCase(examStatus)) {
            throw new InvalidRegistrationException(
                    "Registration for this exam is currently unavailable (Status: " + examStatus + ").");
        }

        if (examRegistrationRepository.existsByStudentIdAndExamId(student.getId(), exam.getId())) {
            throw new DuplicateResourceException("You are already registered for this examination.");
        }

        String regNumber = generateUniqueRegistrationNumber();

        ExamRegistration registration = ExamRegistration.builder()
                .studentId(student.getId())
                .examId(exam.getId())
                .registeredAt(LocalDateTime.now())
                .status(RegistrationStatus.REGISTERED)
                .registrationNumber(regNumber)
                .build();

        ExamRegistration saved = examRegistrationRepository.save(registration);
        return StudentUtil.toRegistrationResponse(saved, student, exam);
    }

    private String generateUniqueRegistrationNumber() {
        String regNumber;
        do {
            regNumber = StudentUtil.generateRegistrationNumber();
        } while (examRegistrationRepository.findByRegistrationNumber(regNumber).isPresent());
        return regNumber;
    }

    @Override
    public List<ExamRegistrationResponse> getMyRegistrations(String usernameOrEmail) {
        Users student = getStudent(usernameOrEmail);
        List<ExamRegistration> registrations = examRegistrationRepository.findByStudentId(student.getId());

        List<String> examIds = registrations.stream().map(ExamRegistration::getExamId).distinct().collect(Collectors.toList());
        Map<String, Exam> examMap = examRepository.findAllById(examIds).stream()
                .collect(Collectors.toMap(Exam::getId, e -> e));

        return registrations.stream()
                .map(reg -> StudentUtil.toRegistrationResponse(reg, student, examMap.get(reg.getExamId())))
                .collect(Collectors.toList());
    }

    @Override
    public ExamRegistrationResponse getRegistrationById(String registrationId, String usernameOrEmail, boolean isAdmin) {
        ExamRegistration reg = examRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with ID: " + registrationId));

        Users student = userRepository.findById(reg.getStudentId()).orElse(null);

        if (!isAdmin) {
            Users authenticatedUser = getStudent(usernameOrEmail);
            if (!authenticatedUser.getId().equals(reg.getStudentId())) {
                throw new AccessDeniedException("You do not have permission to view this registration.");
            }
        }

        Exam exam = examRepository.findById(reg.getExamId()).orElse(null);
        return StudentUtil.toRegistrationResponse(reg, student, exam);
    }

    @Override
    public List<ExamRegistrationResponse> getAllRegistrationsForAdmin() {
        List<ExamRegistration> registrations = examRegistrationRepository.findAll();

        List<String> studentIds = registrations.stream().map(ExamRegistration::getStudentId).distinct().collect(Collectors.toList());
        Map<String, Users> studentMap = userRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Users::getId, u -> u));

        List<String> examIds = registrations.stream().map(ExamRegistration::getExamId).distinct().collect(Collectors.toList());
        Map<String, Exam> examMap = examRepository.findAllById(examIds).stream()
                .collect(Collectors.toMap(Exam::getId, e -> e));

        return registrations.stream()
                .map(reg -> StudentUtil.toRegistrationResponse(reg, studentMap.get(reg.getStudentId()), examMap.get(reg.getExamId())))
                .collect(Collectors.toList());
    }
}

