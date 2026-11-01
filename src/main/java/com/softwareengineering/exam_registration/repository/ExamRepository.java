package com.softwareengineering.exam_registration.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softwareengineering.exam_registration.model.Exam;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {

    List<Exam> findByActiveTrue();

    long countByActiveTrue();
}
