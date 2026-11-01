package com.softwareengineering.exam_registration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softwareengineering.exam_registration.model.Role;
import com.softwareengineering.exam_registration.model.Users;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByGoogleId(String googleId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<Users> findByRole(Role role);

    long countByRole(Role role);

    long countByActiveTrue();
}

