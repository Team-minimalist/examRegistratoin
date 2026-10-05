package com.softwareengineering.exam_registration.service;

import java.util.List;
import com.softwareengineering.exam_registration.model.Users;



public interface StudentService {
    Users registerStudent(Users student);
    Users getStudentById(String Id);
    List<Users> getAllStudents();
    Users updateStudent(String id, Users student);

    void deleteStudent(String id);

}
