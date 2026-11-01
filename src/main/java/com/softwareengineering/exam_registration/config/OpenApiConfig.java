package com.softwareengineering.exam_registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI examRegistrationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Examination Registration API")
                        .description("REST API documentation for Student Examination Registration System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Exam Registration Team")
                                .email("support@examreg.com")));
    }
}

