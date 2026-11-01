package com.softwareengineering.exam_registration.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.service.ExamService;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAvailableExams() {
        List<ExamResponse> exams = examService.getAvailableExams();
        return ResponseEntity.ok(
                ApiResponse.<List<ExamResponse>>builder()
                        .success(true)
                        .message("Available examinations retrieved")
                        .data(exams)
                        .path("/api/exams")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> getExamById(@PathVariable String id) {
        ExamResponse exam = examService.getExamById(id);
        return ResponseEntity.ok(
                ApiResponse.<ExamResponse>builder()
                        .success(true)
                        .message("Examination details retrieved")
                        .data(exam)
                        .path("/api/exams/" + id)
                        .build()
        );
    }
}
