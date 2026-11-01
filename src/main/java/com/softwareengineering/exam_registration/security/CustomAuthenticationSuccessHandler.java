package com.softwareengineering.exam_registration.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.StudentRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final StudentRepository studentRepository;

    public CustomAuthenticationSuccessHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ADMIN"));

        if (isAdmin) {
            response.sendRedirect("/admin/dashboard.html");
            return;
        }

        String principalName = authentication.getName();
        Student student = studentRepository.findByUsername(principalName)
                .or(() -> studentRepository.findByEmail(principalName))
                .orElse(null);

        if (student != null && !student.isProfileCompleted()) {
            response.sendRedirect("/profile.html?status=incomplete");
        } else {
            response.sendRedirect("/dashboard.html");
        }
    }
}
