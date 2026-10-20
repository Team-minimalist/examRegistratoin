package com.softwareengineering.exam_registration.service.imp;

import com.softwareengineering.exam_registration.exception.StudentNotFoundException;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.StudentRepository;
import com.softwareengineering.exam_registration.service.StudentService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(String id, Student student) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setAcademicDetails(student.getAcademicDetails());
        existingStudent.setStudentAddress(student.getStudentAddress());

        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(String id) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        studentRepository.delete(existingStudent);
    }
}