package com.softwareengineering.exam_registration.service.imp;

import com.softwareengineering.exam_registration.model.Exam;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.service.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Exam getExamById(String id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(String id, Exam exam) {

        Exam existingExam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        existingExam.setExamName(exam.getExamName());
        existingExam.setDescription(exam.getDescription());
        existingExam.setExamType(exam.getExamType());
        existingExam.setRegistrationStart(exam.getRegistrationStart());
        existingExam.setRegistrationEnd(exam.getRegistrationEnd());
        existingExam.setExamStart(exam.getExamStart());
        existingExam.setExamEnd(exam.getExamEnd());
        existingExam.setDurationMinutes(exam.getDurationMinutes());
        existingExam.setTotalMarks(exam.getTotalMarks());
        existingExam.setPassingMarks(exam.getPassingMarks());
        existingExam.setStatus(exam.getStatus());

        return examRepository.save(existingExam);
    }

    @Override
    public void deleteExam(String id) {

        Exam existingExam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        examRepository.delete(existingExam);
    }
}