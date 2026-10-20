package com.softwareengineering.exam_registration.service;

import com.softwareengineering.exam_registration.model.ExamRegistration;

import java.util.List;

public interface ExamRegistrationService {

    ExamRegistration getRegistrationById(String id);

    List<ExamRegistration> getAllRegistrations();

    ExamRegistration createRegistration(ExamRegistration registration);

    ExamRegistration updateRegistration(String id, ExamRegistration registration);

    void deleteRegistration(String id);
}