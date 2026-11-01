package com.softwareengineering.exam_registration.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.softwareengineering.exam_registration.model.AuthProvider;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.UserRepository;

@Configuration
public class UserConfig {

    private static final Logger log = LoggerFactory.getLogger(UserConfig.class);

    @Bean
    CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.countByRole(Role.ADMIN) == 0) {
                Users admin = Users.builder()
                        .username("admin")
                        .email("admin@examreg.com")
                        .password(passwordEncoder.encode("Admin@12345"))
                        .name("System Administrator")
                        .role(Role.ADMIN)
                        .active(true)
                        .profileCompleted(true)
                        .authProvider(AuthProvider.LOCAL)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                userRepository.save(admin);
                log.info("Default admin created: username='admin', password='Admin@12345'");
            }
        };
    }
}

