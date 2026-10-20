package com.softwareengineering.exam_registration.repository;

import com.softwareengineering.exam_registration.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByEmail(String email);

    Optional<Student> findByAcademicDetailsRegistrationNumber(String registrationNumber);

    Optional<Student> findByAcademicDetailsRollNumber(String rollNumber);
  
}