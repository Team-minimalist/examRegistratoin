package com.softwareengineering.exam_registration.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.model.AuthProvider;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.UserRepository;

@Service
public class GoogleOAuthService {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuthService.class);
    private final UserRepository userRepository;

    public GoogleOAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Processes or links an authenticated Google OAuth2 user.
     * Follows safe account linking rules:
     * 1. Search by Google ID.
     * 2. If not found, search by email.
     * 3. If found by email, safely link googleId without overwriting password/profile.
     * 4. Otherwise, create a new student account with authProvider=GOOGLE and profileCompleted=false.
     */
    public Users processGoogleUser(OAuth2User oauth2User) {
        String googleId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null) {
            throw new IllegalArgumentException("Google OAuth2 login did not return an email address");
        }

        // 1. Search by Google ID
        if (googleId != null) {
            Optional<Users> userByGoogleId = userRepository.findByGoogleId(googleId);
            if (userByGoogleId.isPresent()) {
                log.info("Existing Google user authenticated: {}", email);
                return userByGoogleId.get();
            }
        }

        // 2. Search by email
        Optional<Users> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            Users existing = userByEmail.get();
            log.info("Safe account linking for email {}: attaching Google ID", email);
            if (existing.getGoogleId() == null && googleId != null) {
                existing.setGoogleId(googleId);
                existing.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(existing);
            }
            return existing;
        }

        // 3. Create new student record for first-time Google login
        log.info("Creating new student account for first-time Google user: {}", email);
        String baseUsername = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
        String uniqueUsername = generateUniqueUsername(baseUsername);

        Users newUser = Users.builder()
                .username(uniqueUsername)
                .email(email)
                .name(name != null && !name.trim().isEmpty() ? name : uniqueUsername)
                .googleId(googleId)
                .authProvider(AuthProvider.GOOGLE)
                .role(Role.STUDENT)
                .active(true)
                .profileCompleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(newUser);
    }

    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername.replaceAll("[^a-zA-Z0-9._-]", "");
        if (username.isEmpty()) {
            username = "student";
        }
        if (!userRepository.existsByUsername(username)) {
            return username;
        }
        int count = 1;
        while (userRepository.existsByUsername(username + count)) {
            count++;
        }
        return username + count;
    }
}

