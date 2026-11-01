package com.softwareengineering.exam_registration.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.model.Admin;
import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Student;
import com.softwareengineering.exam_registration.repository.AdminRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;

    public UserDetailsServiceImp(AdminRepository adminRepository,
                                 StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // 1. Check if user is an Administrator
        Optional<Admin> adminOpt = adminRepository.findByUsername(usernameOrEmail)
                .or(() -> adminRepository.findByEmail(usernameOrEmail));

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new User(
                    admin.getUsername(),
                    admin.getPassword() != null ? admin.getPassword() : "",
                    admin.isActive(),
                    true,
                    true,
                    true,
                    Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
            );
        }

        // 2. Check if user is a Student
        Optional<Student> studentOpt = studentRepository.findByUsername(usernameOrEmail)
                .or(() -> studentRepository.findByEmail(usernameOrEmail));

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            return new User(
                    student.getUsername(),
                    student.getPassword() != null ? student.getPassword() : "",
                    student.isActive(),
                    true,
                    true,
                    true,
                    Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_STUDENT.name()))
            );
        }

        throw new UsernameNotFoundException("No account found matching: " + usernameOrEmail);
    }
}
