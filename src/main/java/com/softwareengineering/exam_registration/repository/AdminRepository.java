package com.softwareengineering.exam_registration.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softwareengineering.exam_registration.model.Admin;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {

    
    Optional<Admin> findByAdminEmail(String adminEmail);
}
