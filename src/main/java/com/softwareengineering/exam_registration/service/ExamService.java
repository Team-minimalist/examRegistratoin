package com.softwareengineering.exam_registration.service;

import com.softwareengineering.exam_registration.model.Exam;

import java.util.List;


public interface ExamService {
    Exam getExamById(String id);

    List<Exam> getAllExams();

    Exam createExam(Exam exam);

    Exam updateExam(String id, Exam exam);

    void deleteExam(String id);
}

