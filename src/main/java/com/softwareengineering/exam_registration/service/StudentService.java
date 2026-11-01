package com.softwareengineering.exam_registration.service;

import java.util.List;

import com.softwareengineering.exam_registration.dto.StudentProfileUpdateRequest;
import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.model.Student;

public interface StudentService {

    StudentResponse registerStudent(StudentRegistrationRequest request);

    StudentResponse getStudentProfile(String usernameOrEmail);

    StudentResponse updateStudentProfile(String usernameOrEmail, StudentProfileUpdateRequest request);

    Student getStudentByUsernameOrEmail(String usernameOrEmail);

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(String id);

    StudentResponse toggleStudentActiveStatus(String id);

    List<StudentResponse> searchStudents(String query);

    void deleteStudent(String id);
}
