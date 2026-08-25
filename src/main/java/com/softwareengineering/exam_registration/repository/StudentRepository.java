package com.softwareengineering.exam_registration.repository;

import com.softwareengineering.exam_registration.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findByContactEmail(String email);

    boolean existsByStudentId(String studentId);

    boolean existsByContactEmail(String email);
}