package com.softwareengineering.exam_registration.controller.StudentController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.service.StudentService.StudentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public Student registerStudent(
            @RequestBody StudentRegistrationRequest request) {

        return studentService.registerStudent(request);
    }
}