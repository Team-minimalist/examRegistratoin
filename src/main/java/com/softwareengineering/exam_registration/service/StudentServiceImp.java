package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
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
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.UserRepository;
import com.softwareengineering.exam_registration.util.StudentUtil;

@Service
public class StudentServiceImp implements StudentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdentificationService identificationService;

    public StudentServiceImp(UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             IdentificationService identificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.identificationService = identificationService;
    }

    @Override
    public StudentResponse registerStudent(StudentRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        Users student = Users.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName().trim())
                .authProvider(AuthProvider.LOCAL)
                .role(Role.STUDENT)
                .active(true)
                .profileCompleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Users saved = userRepository.save(student);
        return StudentUtil.toStudentResponse(saved);
    }

    @Override
    public StudentResponse getStudentProfile(String usernameOrEmail) {
        Users user = getUserByUsernameOrEmail(usernameOrEmail);
        return StudentUtil.toStudentResponse(user);
    }

    @Override
    public StudentResponse updateStudentProfile(String usernameOrEmail, StudentProfileUpdateRequest request) {
        Users user = getUserByUsernameOrEmail(usernameOrEmail);

        identificationService.validateIdentification(request.getIdentificationType(), request.getIdentificationNumber());

        user.setName(request.getName().trim());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setIdentificationType(request.getIdentificationType());
        user.setIdentificationNumber(request.getIdentificationNumber().trim().toUpperCase());
        user.setAcademicDetails(request.getAcademicDetails());
        user.setAddress(request.getAddress());
        user.setProfileCompleted(true);
        user.setUpdatedAt(LocalDateTime.now());

        Users saved = userRepository.save(user);
        return StudentUtil.toStudentResponse(saved);
    }

    @Override
    public Users getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return userRepository.findByRole(Role.STUDENT).stream()
                .map(StudentUtil::toStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return StudentUtil.toStudentResponse(user);
    }

    @Override
    public StudentResponse toggleStudentActiveStatus(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        user.setActive(!user.isActive());
        user.setUpdatedAt(LocalDateTime.now());
        Users saved = userRepository.save(user);
        return StudentUtil.toStudentResponse(saved);
    }

    @Override
    public void deleteStudent(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        userRepository.delete(user);
    }
}

