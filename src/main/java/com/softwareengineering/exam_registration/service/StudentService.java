package com.softwareengineering.exam_registration.service;

import java.util.List;

import com.softwareengineering.exam_registration.dto.StudentProfileUpdateRequest;
import com.softwareengineering.exam_registration.dto.StudentRegistrationRequest;
import com.softwareengineering.exam_registration.dto.StudentResponse;
import com.softwareengineering.exam_registration.model.Users;

public interface StudentService {

    StudentResponse registerStudent(StudentRegistrationRequest request);

    StudentResponse getStudentProfile(String usernameOrEmail);

    StudentResponse updateStudentProfile(String usernameOrEmail, StudentProfileUpdateRequest request);

    Users getUserByUsernameOrEmail(String usernameOrEmail);

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(String id);

    StudentResponse toggleStudentActiveStatus(String id);

    void deleteStudent(String id);
}
