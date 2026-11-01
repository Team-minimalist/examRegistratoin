package com.softwareengineering.exam_registration.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softwareengineering.exam_registration.dto.AdminResponse;
import com.softwareengineering.exam_registration.dto.AdminStatsResponse;
import com.softwareengineering.exam_registration.dto.ApiResponse;
import com.softwareengineering.exam_registration.dto.ExamRegistrationResponse;
import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.service.AdminService;
import com.softwareengineering.exam_registration.service.ExamRegistrationService;
import com.softwareengineering.exam_registration.service.ExamService;
import com.softwareengineering.exam_registration.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ExamService examService;
    private final StudentService studentService;
    private final ExamRegistrationService examRegistrationService;

    public AdminController(AdminService adminService,
                           ExamService examService,
                           StudentService studentService,
                           ExamRegistrationService examRegistrationService) {
        this.adminService = adminService;
        this.examService = examService;
        this.studentService = studentService;
        this.examRegistrationService = examRegistrationService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminResponse>> getMyProfile(Authentication authentication) {
        AdminResponse admin = adminService.getAdminProfile(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.<AdminResponse>builder()
                        .success(true)
                        .message("Admin profile retrieved")
                        .data(admin)
                        .path("/api/admin/me")
                        .build()
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getAdminStats() {
        AdminStatsResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(
                ApiResponse.<AdminStatsResponse>builder()
                        .success(true)
                        .message("Admin dashboard statistics retrieved")
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
                        .message("Examination active status toggled successfully")
                        .data(updated)
                        .path("/api/admin/exams/" + id + "/status")
                        .build()
        );
    }

    @PatchMapping("/exams/{id}/publish")
    public ResponseEntity<ApiResponse<ExamResponse>> toggleExamPublishStatus(@PathVariable String id) {
        ExamResponse updated = examService.toggleExamPublishStatus(id);
        return ResponseEntity.ok(
                ApiResponse.<ExamResponse>builder()
                        .success(true)
                        .message("Examination published status toggled successfully")
                        .data(updated)
                        .path("/api/admin/exams/" + id + "/publish")
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

    @GetMapping("/students/search")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> searchStudents(
            @RequestParam(required = false) String query) {
        List<StudentResponse> students = studentService.searchStudents(query);
        return ResponseEntity.ok(
                ApiResponse.<List<StudentResponse>>builder()
                        .success(true)
                        .message("Student search results retrieved")
                        .data(students)
                        .path("/api/admin/students/search")
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

    @PatchMapping("/registrations/{id}/cancel")
    public ResponseEntity<ApiResponse<ExamRegistrationResponse>> cancelRegistration(
            @PathVariable String id,
            Authentication authentication) {
        ExamRegistrationResponse response = examRegistrationService.cancelRegistration(id, authentication.getName(), true);
        return ResponseEntity.ok(
                ApiResponse.<ExamRegistrationResponse>builder()
                        .success(true)
                        .message("Registration cancelled by administrator")
                        .data(response)
                        .path("/api/admin/registrations/" + id + "/cancel")
                        .build()
        );
    }
}
