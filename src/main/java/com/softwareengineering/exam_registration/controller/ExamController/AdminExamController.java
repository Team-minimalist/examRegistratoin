package com.softwareengineering.exam_registration.controller.ExamController;

@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
public class AdminExamController {

    private final ExamApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ExamApplicationResponseDto>> getApplications(
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.getApplicationsByFilter(examId, status));
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ExamApplicationResponseDto> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody @Valid ApplicationStatusUpdateDto dto) {
        return ResponseEntity.ok(applicationService.updateStatus(applicationId, dto));
    }

    @GetMapping("/{applicationId}/document")
    public ResponseEntity<Resource> downloadStudentDocument(@PathVariable Long applicationId) {
        Resource file = applicationService.getStudentDocument(applicationId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}