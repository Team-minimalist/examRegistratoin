package com.softwareengineering.exam_registration.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.service.StudentService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable String id) {

        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // Create student
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {

        Student student = new Student();

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setStudentAddress(request.getStudentAddress());
        student.setAcademicDetails(request.getStudentAcademicDetails());

        return ResponseEntity.ok(studentService.createStudent(student));
    }
}