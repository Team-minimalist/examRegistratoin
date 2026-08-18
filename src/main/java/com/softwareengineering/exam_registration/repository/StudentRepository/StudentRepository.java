package com.softwareengineering.exam_registration.repository.StudentRepository;

import java.util.Optional;

import com.softwareengineering.exam_registration.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student,String> {

}