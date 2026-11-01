package com.softwareengineering.exam_registration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.softwareengineering.exam_registration.service.AdminService;

@Configuration
public class UserConfig {

    private static final Logger log = LoggerFactory.getLogger(UserConfig.class);

    @Value("${app.admin.initial.email:admin@examportal.gov.in}")
    private String initialAdminEmail;

    @Value("${app.admin.initial.password:Admin@12345}")
    private String initialAdminPassword;

    @Value("${app.admin.initial.name:System Administrator}")
    private String initialAdminName;

    @Value("${app.admin.initial.username:admin}")
    private String initialAdminUsername;

    @Bean
    CommandLineRunner initDefaultAdmin(AdminService adminService) {
        return args -> {
            adminService.bootstrapInitialAdmin(
                    initialAdminEmail,
                    initialAdminPassword,
                    initialAdminName,
                    initialAdminUsername
            );
            log.info("Initial admin check/bootstrap completed for: {}", initialAdminEmail);
        };
    }
}
