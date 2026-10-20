package com.softwareengineering.exam_registration.service.imp;

import com.softwareengineering.exam_registration.exception.ExamRegistrationNotFoundException;
import com.softwareengineering.exam_registration.exception.StudentNotFoundException;
import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;
import com.softwareengineering.exam_registration.service.ExamRegistrationService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamRegistrationServiceImpl
        implements ExamRegistrationService {

    private final ExamRegistrationRepository examRegistrationRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    public ExamRegistrationServiceImpl(
            ExamRegistrationRepository examRegistrationRepository, ExamRepository examRepository,StudentRepository studentRepository ) {

        this.examRegistrationRepository = examRegistrationRepository;
        this.examRepository = examRepository;
        this.studentRepository = null;
    }

    @Override
    public ExamRegistration getRegistrationById(String id) {

        return examRegistrationRepository.findById(id)
                .orElseThrow(() ->
                        new ExamRegistrationNotFoundException(
                                "Exam registration not found"));
    }

    @Override
    public List<ExamRegistration> getAllRegistrations() {

        return examRegistrationRepository.findAll();
    }

    @Override
    public ExamRegistration createRegistration(
            ExamRegistration registration) {
        
        studentRepository.findById(registration.getStudentId())
                        .orElseThrow(() -> new StudentNotFoundException("Student Not Found"));
        
        examRepository.findById(registration.getExamId())
                    .orElseThrow(() -> new Exam)
        return examRegistrationRepository.save(registration);
    }

    @Override
    public ExamRegistration updateRegistration(
            String id,
            ExamRegistration registration) {

        ExamRegistration existingRegistration =
                examRegistrationRepository.findById(id)
                        .orElseThrow(() ->
                                new ExamRegistrationNotFoundException(
                                        "Exam registration not found"));

        existingRegistration.setStudentId(
                registration.getStudentId());

        existingRegistration.setExamId(
                registration.getExamId());

        existingRegistration.setRegistrationDate(
                registration.getRegistrationDate());

        existingRegistration.setStatus(
                registration.getStatus());

        return examRegistrationRepository.save(existingRegistration);
    }

    @Override
    public void deleteRegistration(String id) {

        ExamRegistration existingRegistration =
                examRegistrationRepository.findById(id)
                        .orElseThrow(() ->
                                new ExamRegistrationNotFoundException(
                                        "Exam registration not found"));

        examRegistrationRepository.delete(existingRegistration);
    }
}