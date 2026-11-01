package com.softwareengineering.exam_registration.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.service.GoogleOAuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final GoogleOAuthService googleOAuthService;

    public CustomOAuth2SuccessHandler(GoogleOAuthService googleOAuthService) {
        this.googleOAuthService = googleOAuthService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            Student student = googleOAuthService.processGoogleStudent(oauth2User);

            if (!student.isProfileCompleted()) {
                response.sendRedirect("/profile.html?status=incomplete");
            } else {
                response.sendRedirect("/dashboard.html");
            }
        } else {
            response.sendRedirect("/dashboard.html");
        }
    }
}
