package com.softwareengineering.exam_registration.service.imp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.model.Users;
import com.softwareengineering.exam_registration.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Users users = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                        "User not found: " + username
                    )
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(users.getUsername())
                .password(users.getPassword())
                .roles("USER")
                .build();
    }
}