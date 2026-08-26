package com.softwareengineering.exam_registration.service;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.exception.DuplicateEmailException;
import com.softwareengineering.exam_registration.exception.DuplicateIdentificationException;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.CounterRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CounterRepository counterRepository;

    public StudentService(
            StudentRepository studentRepository,
            CounterRepository counterRepository) {

        this.studentRepository = studentRepository;
        this.counterRepository = counterRepository;
    }

    public Student registerStudent(StudentRegistrationRequest request) {

        String email = request.getContact().getEmail();
        String identificationNumber =
                request.getIdentification().getNumber();

        // Check duplicate email
        if (studentRepository.existsByContact_Email(email)) {
            throw new DuplicateEmailException(
                    "A student with this email already exists"
            );
        }

        // Check duplicate identification
        if (studentRepository.existsByIdentification_Number(identificationNumber)) {

            throw new DuplicateIdentificationException(
                    "A student with this identification number already exists"
            );
        }

        Student student = new Student();

        student.setPersonalInfo(request.getPersonalInfo());
        student.setEducation(request.getEducation());
        student.setIdentification(request.getIdentification());
        student.setContact(request.getContact());
        student.setAddress(request.getAddress());

        // Generate unique student ID
        long sequence =
                counterRepository.getNextSequence("student");

        String studentId = String.format(
                "STU2026%06d",
                sequence
        );

        student.setStudentId(studentId);
        student.setCreatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }
}