package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.model.AuthProvider;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.StudentRepository;

@Service
public class GoogleOAuthService {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuthService.class);
    private final StudentRepository studentRepository;

    public GoogleOAuthService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student processGoogleStudent(OAuth2User oauth2User) {
        String googleId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null) {
            throw new IllegalArgumentException("Google identity did not provide a verified email address.");
        }

        // 1. Search existing student by Google ID
        if (googleId != null) {
            Optional<Student> studentByGoogleId = studentRepository.findByGoogleId(googleId);
            if (studentByGoogleId.isPresent()) {
                log.info("Student authenticated via Google: {}", email);
                return studentByGoogleId.get();
            }
        }

        // 2. Search existing student by verified email
        Optional<Student> studentByEmail = studentRepository.findByEmail(email);
        if (studentByEmail.isPresent()) {
            Student existing = studentByEmail.get();
            log.info("Safe linking student account for email: {}", email);
            if (existing.getGoogleId() == null && googleId != null) {
                existing.setGoogleId(googleId);
                existing.setUpdatedAt(LocalDateTime.now());
                return studentRepository.save(existing);
            }
            return existing;
        }

        // 3. Provision new student account for first-time Google user
        log.info("Provisioning new student account for Google user: {}", email);
        String baseUsername = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
        String uniqueUsername = generateUniqueStudentUsername(baseUsername);

        Student newStudent = Student.builder()
                .username(uniqueUsername)
                .email(email)
                .name(name != null && !name.trim().isEmpty() ? name : uniqueUsername)
                .googleId(googleId)
                .authProvider(AuthProvider.GOOGLE)
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_STUDENT)))
                .active(true)
                .profileCompleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return studentRepository.save(newStudent);
    }

    private String generateUniqueStudentUsername(String baseUsername) {
        String username = baseUsername.replaceAll("[^a-zA-Z0-9._-]", "");
        if (username.isEmpty()) {
            username = "student";
        }
        if (!studentRepository.existsByUsername(username)) {
            return username;
        }
        int count = 1;
        while (studentRepository.existsByUsername(username + count)) {
            count++;
        }
        return username + count;
    }
}
