package com.softwareengineering.exam_registration.controller;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("/register")
    public StudentRegistrationRequest registerStudent(
           @Valid  @RequestBody StudentRegistrationRequest request) {

        return request;
    }
}