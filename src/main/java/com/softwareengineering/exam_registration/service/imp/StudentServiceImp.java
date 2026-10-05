package com.softwareengineering.exam_registration.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.UserRepository;
import com.softwareengineering.exam_registration.service.StudentService;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class StudentServiceImp implements StudentService {
    private final UserRepository userRepository;

    @Override 
    public Users registerStudent(Users student) {
        return userRepository.save(student);
    }

    @Override 
    public Users getStudentById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override 
    public List<Users> getAllStudents() {
        return userRepository.findAll();
    }

    @Override 
    public Users updateStudent(String id, Users student) {
        if (userRepository.existsById(id)) {
            student.setId(id);
            return userRepository.save(student);
        }
        return null;
    }

    @Override 
    public void deleteStudent(String id) {
        userRepository.deleteById(id);
    }
}

