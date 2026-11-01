package com.softwareengineering.exam_registration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.UserRepository;
import com.softwareengineering.exam_registration.service.ExamRegistrationService;
import com.softwareengineering.exam_registration.service.ExamService;
import com.softwareengineering.exam_registration.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ExamService examService;
    private final StudentService studentService;
    private final ExamRegistrationService examRegistrationService;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final ExamRegistrationRepository examRegistrationRepository;

    public AdminController(ExamService examService,
                           StudentService studentService,
                           ExamRegistrationService examRegistrationService,
                           UserRepository userRepository,
                           ExamRepository examRepository,
                           ExamRegistrationRepository examRegistrationRepository) {
        this.examService = examService;
        this.studentService = studentService;
        this.examRegistrationService = examRegistrationService;
        this.userRepository = userRepository;
        this.examRepository = examRepository;
        this.examRegistrationRepository = examRegistrationRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long activeStudents = userRepository.findByRole(Role.STUDENT).stream().filter(u -> u.isActive()).count();
        long totalExams = examRepository.count();
        long activeExams = examRepository.countByActiveTrue();
        List<ExamResponse> allExams = examService.getAllExamsForAdmin();
        long openExams = allExams.stream().filter(e -> "OPEN".equalsIgnoreCase(e.getRegistrationStatus())).count();
        long totalRegistrations = examRegistrationRepository.count();

        stats.put("totalStudents", totalStudents);
        stats.put("activeStudents", activeStudents);
        stats.put("totalExams", totalExams);
        stats.put("activeExams", activeExams);
        stats.put("openExams", openExams);
        stats.put("totalRegistrations", totalRegistrations);

        return ResponseEntity.ok(
                ApiResponse.<Map<String, Object>>builder()
                        .success(true)
                        .message("Admin statistics retrieved")
                        .data(stats)
                        .path("/api/admin/stats")
                        .build()
        );
    }

    // EXAM MANAGEMENT
    @GetMapping("/exams")
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAllExams() {
        List<ExamResponse> exams = examService.getAllExamsForAdmin();
        return ResponseEntity.ok(
                ApiResponse.<List<ExamResponse>>builder()
                        .success(true)
                        .message("All examinations retrieved")
                        .data(exams)
                        .path("/api/admin/exams")
                        .build()
        );
    }

    @PostMapping("/exams")
    public ResponseEntity<ApiResponse<ExamResponse>> createExam(
            @Valid @RequestBody ExamRequest request) {

        ExamResponse created = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ExamResponse>builder()
                        .success(true)
                        .message("Examination created successfully")
                        .data(created)
                        .path("/api/admin/exams")
                        .build()
        );
    }

    @PutMapping("/exams/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> updateExam(
            @PathVariable String id,
            @Valid @RequestBody ExamRequest request) {

        ExamResponse updated = examService.updateExam(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ExamResponse>builder()
                        .success(true)
                        .message("Examination updated successfully")
                        .data(updated)
                        .path("/api/admin/exams/" + id)
                        .build()
        );
    }

    @PatchMapping("/exams/{id}/status")
    public ResponseEntity<ApiResponse<ExamResponse>> toggleExamStatus(@PathVariable String id) {
        ExamResponse updated = examService.toggleExamStatus(id);
        return ResponseEntity.ok(
                ApiResponse.<ExamResponse>builder()
                        .success(true)
                        .message("Examination status toggled successfully")
                        .data(updated)
                        .path("/api/admin/exams/" + id + "/status")
                        .build()
        );
    }

    @DeleteMapping("/exams/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExam(@PathVariable String id) {
        examService.deleteExam(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Examination deleted successfully")
                        .path("/api/admin/exams/" + id)
                        .build()
        );
    }

    // STUDENT MANAGEMENT
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(
                ApiResponse.<List<StudentResponse>>builder()
                        .success(true)
                        .message("Students list retrieved")
                        .data(students)
                        .path("/api/admin/students")
                        .build()
        );
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable String id) {
        StudentResponse student = studentService.getStudentById(id);
        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Student retrieved")
                        .data(student)
                        .path("/api/admin/students/" + id)
                        .build()
        );
    }

    @PatchMapping("/students/{id}/status")
    public ResponseEntity<ApiResponse<StudentResponse>> toggleStudentStatus(@PathVariable String id) {
        StudentResponse updated = studentService.toggleStudentActiveStatus(id);
        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Student status updated")
                        .data(updated)
                        .path("/api/admin/students/" + id + "/status")
                        .build()
        );
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Student deleted successfully")
                        .path("/api/admin/students/" + id)
                        .build()
        );
    }

    // REGISTRATION MANAGEMENT
    @GetMapping("/registrations")
    public ResponseEntity<ApiResponse<List<ExamRegistrationResponse>>> getAllRegistrations() {
        List<ExamRegistrationResponse> registrations = examRegistrationService.getAllRegistrationsForAdmin();
        return ResponseEntity.ok(
                ApiResponse.<List<ExamRegistrationResponse>>builder()
                        .success(true)
                        .message("All examination registrations retrieved")
                        .data(registrations)
                        .path("/api/admin/registrations")
                        .build()
        );
    }
}
