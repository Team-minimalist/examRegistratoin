package com.softwareengineering.exam_registration.service;

import java.util.List;

import com.softwareengineering.exam_registration.dto.ExamRequest;
import com.softwareengineering.exam_registration.dto.ExamResponse;
import com.softwareengineering.exam_registration.model.Exam;

public interface ExamService {

    List<ExamResponse> getAvailableExams();

    List<ExamResponse> getAllExamsForAdmin();

    ExamResponse getExamById(String id);

    Exam getExamEntityById(String id);

    ExamResponse createExam(ExamRequest request);

    ExamResponse updateExam(String id, ExamRequest request);

    ExamResponse toggleExamStatus(String id);

    ExamResponse toggleExamPublishStatus(String id);

    void deleteExam(String id);
}
