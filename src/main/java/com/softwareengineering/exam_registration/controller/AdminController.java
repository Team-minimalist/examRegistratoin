package com.softwareengineering.exam_registration.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softwareengineering.exam_registration.dto.AdminRegistrationRequest;
import com.softwareengineering.exam_registration.model.Admin;
import com.softwareengineering.exam_registration.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // CREATE ADMIN
    @PostMapping
    public ResponseEntity<Admin> createAdmin(
            @Valid @RequestBody AdminRegistrationRequest request) {

        Admin admin = new Admin();

        admin.setAdminFirstName(request.getAdminFirstName());
        admin.setAdminLastName(request.getAdminLastName());
        admin.setAdminEmail(request.getAdminEmail());
        admin.setAdminPhoneNo(request.getAdminPhoneNo());
        admin.setAdminRole(request.getAdminRole());
        admin.setAdminAddress(request.getAdminAddress());

        return ResponseEntity.ok(
                adminService.createAdmin(admin)
        );
    }

    // GET ALL ADMINS
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {

        return ResponseEntity.ok(
                adminService.getAllAdmin()
        );
    }

    // GET ADMIN BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                adminService.getByAdminId(id)
        );
    }

    // UPDATE ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(
            @PathVariable String id,
            @Valid @RequestBody AdminRegistrationRequest request) {

        Admin admin = new Admin();

        admin.setAdminFirstName(request.getAdminFirstName());
        admin.setAdminLastName(request.getAdminLastName());
        admin.setAdminEmail(request.getAdminEmail());
        admin.setAdminPhoneNo(request.getAdminPhoneNo());
        admin.setAdminRole(request.getAdminRole());
        admin.setAdminAddress(request.getAdminAddress());

        return ResponseEntity.ok(
                adminService.updateAdmin(id, admin)
        );
    }

    // DELETE ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(
            @PathVariable String id) {

        adminService.deleteAdmin(id);

        return ResponseEntity.noContent().build();
    }
}