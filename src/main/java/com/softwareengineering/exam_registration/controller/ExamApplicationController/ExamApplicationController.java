package com.softwareengineering.exam_registration.controller.ExamApplicationController;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ExamApplicationController {

    private final ExamApplicationService applicationService;

    @PostMapping("/request-otp")
    public ResponseEntity<String> requestOtpForExam(@RequestBody @Valid OtpRequestDto dto) {
        applicationService.generateAndSendOtp(dto.getStudentUniqueId(), dto.getExamId());
        return ResponseEntity.ok("OTP has been sent to your registered email address.");
    }

    @PostMapping("/apply")
    public ResponseEntity<ExamApplicationResponseDto> applyForExam(@RequestBody @Valid ApplyExamRequestDto dto) {
        ExamApplicationResponseDto response = applicationService.applyForExam(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}