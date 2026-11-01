package com.softwareengineering.exam_registration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softwareengineering.exam_registration.model.ExamRegistration;
import com.softwareengineering.exam_registration.model.RegistrationStatus;

@Repository
public interface ExamRegistrationRepository extends MongoRepository<ExamRegistration, String> {

    List<ExamRegistration> findByStudentId(String studentId);

    List<ExamRegistration> findByExamId(String examId);

    Optional<ExamRegistration> findByStudentIdAndExamId(String studentId, String examId);

    boolean existsByStudentIdAndExamId(String studentId, String examId);

    Optional<ExamRegistration> findByRegistrationNumber(String registrationNumber);

    long countByStatus(RegistrationStatus status);
}
