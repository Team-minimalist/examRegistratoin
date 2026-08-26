package com.softwareengineering.exam_registration.repository;

import com.softwareengineering.exam_registration.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {

    boolean existsByStudentId(String studentId);

    boolean existsByContact_Email(String email);

    boolean existsByIdentification_Number(String number);
}