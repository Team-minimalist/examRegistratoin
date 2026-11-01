package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.dto.StudentProfileUpdateRequest;
import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.exception.DuplicateResourceException;
import com.softwareengineering.exam_registration.exception.ResourceNotFoundException;
import com.softwareengineering.exam_registration.model.AuthProvider;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.StudentRepository;
import com.softwareengineering.exam_registration.util.StudentUtil;

@Service
public class StudentServiceImp implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdentificationService identificationService;

    public StudentServiceImp(StudentRepository studentRepository,
                             PasswordEncoder passwordEncoder,
                             IdentificationService identificationService) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.identificationService = identificationService;
    }

    @Override
    public StudentResponse registerStudent(StudentRegistrationRequest request) {
        if (studentRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        Student student = Student.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName().trim())
                .authProvider(AuthProvider.LOCAL)
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_STUDENT)))
                .active(true)
                .profileCompleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Student saved = studentRepository.save(student);
        return StudentUtil.toStudentResponse(saved);
    }

    @Override
    public StudentResponse getStudentProfile(String usernameOrEmail) {
        Student student = getStudentByUsernameOrEmail(usernameOrEmail);
        return StudentUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse updateStudentProfile(String usernameOrEmail, StudentProfileUpdateRequest request) {
        Student student = getStudentByUsernameOrEmail(usernameOrEmail);

        identificationService.validateIdentification(
                request.getIdentificationType(),
                request.getIdentificationNumber(),
                request.getDateOfBirth()
        );

        student.setName(request.getName().trim());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setIdentificationType(request.getIdentificationType());
        student.setIdentificationNumber(request.getIdentificationNumber().trim().toUpperCase());
        student.setContactNumber(request.getContactNumber());
        student.setAcademicDetails(request.getAcademicDetails());
        student.setAddress(request.getAddress());
        student.setProfileCompleted(true);
        student.setUpdatedAt(LocalDateTime.now());

        Student saved = studentRepository.save(student);
        return StudentUtil.toStudentResponse(saved);
    }

    @Override
    public Student getStudentByUsernameOrEmail(String usernameOrEmail) {
        return studentRepository.findByUsername(usernameOrEmail)
                .or(() -> studentRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with username or email: " + usernameOrEmail));
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentUtil::toStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return StudentUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse toggleStudentActiveStatus(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        student.setActive(!student.isActive());
        student.setUpdatedAt(LocalDateTime.now());
        Student saved = studentRepository.save(student);
        return StudentUtil.toStudentResponse(saved);
    }

    @Override
    public List<StudentResponse> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        String q = query.trim();
        return studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(q, q, q)
                .stream()
                .map(StudentUtil::toStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        studentRepository.delete(student);
    }
}
