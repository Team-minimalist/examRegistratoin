package com.softwareengineering.exam_registration.controller;

import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.service.ExamService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<Exam> createExam(
            @Valid @RequestBody ExamRequest request) {

        Exam exam = new Exam();

        exam.setExamName(request.getExamName());
        exam.setDescription(request.getDescription());
        exam.setExamType(request.getExamType());
        exam.setRegistrationStart(request.getRegistrationStart());
        exam.setRegistrationEnd(request.getRegistrationEnd());
        exam.setExamStart(request.getExamStart());
        exam.setExamEnd(request.getExamEnd());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());
        exam.setStatus(request.getStatus());

        return ResponseEntity.ok(examService.createExam(exam));
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {

        return ResponseEntity.ok(
                examService.getAllExams()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                examService.getExamById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(
            @PathVariable String id,
            @Valid @RequestBody ExamRequest request) {

        Exam exam = new Exam();

        exam.setExamName(request.getExamName());
        exam.setDescription(request.getDescription());
        exam.setExamType(request.getExamType());
        exam.setRegistrationStart(request.getRegistrationStart());
        exam.setRegistrationEnd(request.getRegistrationEnd());
        exam.setExamStart(request.getExamStart());
        exam.setExamEnd(request.getExamEnd());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());
        exam.setStatus(request.getStatus());

        return ResponseEntity.ok(
                examService.updateExam(id, exam)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(
            @PathVariable String id) {

        examService.deleteExam(id);

        return ResponseEntity.noContent().build();
    }
}