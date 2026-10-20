package com.softwareengineering.exam_registration.controller;

import com.softwareengineering.exam_registration.dto.ExamRegistrationRequest;
import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.service.ExamRegistrationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/exam-registrations")
public class ExamRegistrationController {

    private final ExamRegistrationService examRegistrationService;

    public ExamRegistrationController(
            ExamRegistrationService examRegistrationService) {

        this.examRegistrationService = examRegistrationService;
    }

    @PostMapping
    public ResponseEntity<ExamRegistration> createRegistration(
            @Valid @RequestBody ExamRegistrationRequest request) {

        ExamRegistration registration = new ExamRegistration();

        registration.setStudentId(request.getStudentId());
        registration.setExamId(request.getExamId());

        registration.setRegistrationDate(
                LocalDateTime.now());

        registration.setStatus(request.getStatus());

        return ResponseEntity.ok(
                examRegistrationService.createRegistration(registration));
    }

    @GetMapping
    public ResponseEntity<List<ExamRegistration>>
    getAllRegistrations() {

        return ResponseEntity.ok(
                examRegistrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamRegistration> getRegistrationById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                examRegistrationService.getRegistrationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamRegistration> updateRegistration(
            @PathVariable String id,
            @Valid @RequestBody ExamRegistrationRequest request) {

        ExamRegistration registration = new ExamRegistration();

        registration.setStudentId(request.getStudentId());
        registration.setExamId(request.getExamId());
        registration.setRegistrationDate(
                LocalDateTime.now());
        registration.setStatus(request.getStatus());

        return ResponseEntity.ok(
                examRegistrationService.updateRegistration(
                        id, registration));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(
            @PathVariable String id) {

        examRegistrationService.deleteRegistration(id);

        return ResponseEntity.noContent().build();
    }
}