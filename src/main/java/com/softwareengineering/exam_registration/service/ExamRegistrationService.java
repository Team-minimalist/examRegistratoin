package com.softwareengineering.exam_registration.service;

import java.util.List;

import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;

public interface ExamRegistrationService {

    ExamRegistrationResponse registerForExam(String examId, String usernameOrEmail);

    List<ExamRegistrationResponse> getMyRegistrations(String usernameOrEmail);

    ExamRegistrationResponse getRegistrationById(String registrationId, String usernameOrEmail, boolean isAdmin);

    List<ExamRegistrationResponse> getAllRegistrationsForAdmin();

    ExamRegistrationResponse cancelRegistration(String registrationId, String usernameOrEmail, boolean isAdmin);
}
