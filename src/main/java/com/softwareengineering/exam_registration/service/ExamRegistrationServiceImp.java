package com.softwareengineering.exam_registration.service;

import java.time.LocalDate;
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
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;
import com.softwareengineering.exam_registration.util.RegistrationNumberGenerator;
import com.softwareengineering.exam_registration.util.StudentUtil;

@Service
public class ExamRegistrationServiceImp implements ExamRegistrationService {

    private final ExamRegistrationRepository examRegistrationRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    public ExamRegistrationServiceImp(ExamRegistrationRepository examRegistrationRepository,
                                      ExamRepository examRepository,
                                      StudentRepository studentRepository) {
        this.examRegistrationRepository = examRegistrationRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
    }

    private Student getStudent(String usernameOrEmail) {
        return studentRepository.findByUsername(usernameOrEmail)
                .or(() -> studentRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + usernameOrEmail));
    }

    @Override
    public ExamRegistrationResponse registerForExam(String examId, String usernameOrEmail) {
        Student student = getStudent(usernameOrEmail);

        if (!student.isActive()) {
            throw new InvalidRegistrationException("Your student account is deactivated. Please contact administration.");
        }

        if (!student.isProfileCompleted()) {
            throw new InvalidRegistrationException(
                    "Please complete your personal and academic profile before registering for an examination.");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Examination not found with ID: " + examId));

        if (!exam.isActive() || !exam.isPublished()) {
            throw new InvalidRegistrationException("This examination is currently not available for registration.");
        }

        String examStatus = StudentUtil.calculateExamStatus(exam);
        if (!"OPEN".equalsIgnoreCase(examStatus)) {
            throw new InvalidRegistrationException(
                    "Registration for this exam is closed or has not started yet (Current Status: " + examStatus + ").");
        }

        if (exam.getMaximumCandidates() != null && exam.getMaximumCandidates() > 0) {
            long currentCount = examRegistrationRepository.countByExamId(exam.getId());
            if (currentCount >= exam.getMaximumCandidates()) {
                throw new InvalidRegistrationException("This examination has reached its maximum candidate capacity.");
            }
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
                .paymentStatus((exam.getApplicationFee() != null && exam.getApplicationFee() > 0) ? "PAID" : "EXEMPT")
                .updatedAt(LocalDateTime.now())
                .build();

        ExamRegistration saved = examRegistrationRepository.save(registration);
        return StudentUtil.toRegistrationResponse(saved, student, exam);
    }

    private String generateUniqueRegistrationNumber() {
        String regNumber;
        do {
            regNumber = RegistrationNumberGenerator.generateRegistrationNumber();
        } while (examRegistrationRepository.findByRegistrationNumber(regNumber).isPresent());
        return regNumber;
    }

    @Override
    public List<ExamRegistrationResponse> getMyRegistrations(String usernameOrEmail) {
        Student student = getStudent(usernameOrEmail);
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
                .orElseThrow(() -> new ResourceNotFoundException("Registration record not found with ID: " + registrationId));

        Student student = studentRepository.findById(reg.getStudentId()).orElse(null);

        if (!isAdmin) {
            Student authenticatedUser = getStudent(usernameOrEmail);
            if (!authenticatedUser.getId().equals(reg.getStudentId())) {
                throw new AccessDeniedException("You do not have permission to view this registration record.");
            }
        }

        Exam exam = examRepository.findById(reg.getExamId()).orElse(null);
        return StudentUtil.toRegistrationResponse(reg, student, exam);
    }

    @Override
    public List<ExamRegistrationResponse> getAllRegistrationsForAdmin() {
        List<ExamRegistration> registrations = examRegistrationRepository.findAll();

        List<String> studentIds = registrations.stream().map(ExamRegistration::getStudentId).distinct().collect(Collectors.toList());
        Map<String, Student> studentMap = studentRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Student::getId, u -> u));

        List<String> examIds = registrations.stream().map(ExamRegistration::getExamId).distinct().collect(Collectors.toList());
        Map<String, Exam> examMap = examRepository.findAllById(examIds).stream()
                .collect(Collectors.toMap(Exam::getId, e -> e));

        return registrations.stream()
                .map(reg -> StudentUtil.toRegistrationResponse(reg, studentMap.get(reg.getStudentId()), examMap.get(reg.getExamId())))
                .collect(Collectors.toList());
    }

    @Override
    public ExamRegistrationResponse cancelRegistration(String registrationId, String usernameOrEmail, boolean isAdmin) {
        ExamRegistration reg = examRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration record not found with ID: " + registrationId));

        Student student = studentRepository.findById(reg.getStudentId()).orElse(null);

        if (!isAdmin) {
            Student authenticatedUser = getStudent(usernameOrEmail);
            if (!authenticatedUser.getId().equals(reg.getStudentId())) {
                throw new AccessDeniedException("You do not have permission to cancel this registration.");
            }
        }

        if (reg.getStatus() == RegistrationStatus.CANCELLED) {
            throw new InvalidRegistrationException("This registration is already cancelled.");
        }

        Exam exam = examRepository.findById(reg.getExamId()).orElse(null);
        if (exam != null && exam.getExaminationDate() != null && LocalDate.now().isAfter(exam.getExaminationDate())) {
            throw new InvalidRegistrationException("Cannot cancel registration for an examination that has already taken place.");
        }

        reg.setStatus(RegistrationStatus.CANCELLED);
        reg.setUpdatedAt(LocalDateTime.now());
        ExamRegistration saved = examRegistrationRepository.save(reg);

        return StudentUtil.toRegistrationResponse(saved, student, exam);
    }
}
