package com.softwareengineering.exam_registration.service;

import java.util.List;
import com.softwareengineering.exam_registration.model.Student;


public interface StudentService {

    Student getStudentById(String Id);
    List<Student> getAllStudents();
    Student updateStudent(String id, Student student);
    Student createStudent(Student student);
    void deleteStudent(String id);

}
