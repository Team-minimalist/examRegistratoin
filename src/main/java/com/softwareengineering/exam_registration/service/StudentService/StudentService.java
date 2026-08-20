package com.softwareengineering.exam_registration.service.StudentService;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.StudentRepository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }
    
    public Student registerStudent(StudentRegistrationRequest request){
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setPassword(request.getPassword());

        return studentRepository.save(student);

    }

}
