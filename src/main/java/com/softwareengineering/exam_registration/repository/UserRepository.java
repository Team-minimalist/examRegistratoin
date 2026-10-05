package com.softwareengineering.exam_registration.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.softwareengineering.exam_registration.model.Users;

public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);
}

