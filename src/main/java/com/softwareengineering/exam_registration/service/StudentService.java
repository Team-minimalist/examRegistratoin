package com.softwareengineering.exam_registration.service;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
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

        Student student = new Student();

        student.setPersonalInfo(request.getPersonalInfo());
        student.setEducation(request.getEducation());
        student.setIdentification(request.getIdentification());
        student.setContact(request.getContact());
        student.setAddress(request.getAddress());

        long sequence = counterRepository.getNextSequence("student");

        String studentId = String.format(
                "STU2026%06d",
                sequence
        );

        student.setStudentId(studentId);

        student.setCreatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }
}