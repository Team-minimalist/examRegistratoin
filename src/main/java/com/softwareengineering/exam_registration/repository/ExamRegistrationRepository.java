package com.softwareengineering.exam_registration.repository;

import com.softwareengineering.exam_registration.model.ExamRegistration;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRegistrationRepository extends MongoRepository<ExamRegistration, String> {
    Optional<ExamRegistration> findByStudentIdAndExamId(
        String studentId,
        String examId
    );
}