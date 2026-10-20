package com.softwareengineering.exam_registration.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.softwareengineering.exam_registration.exception.AdminNotFoundException;
import com.softwareengineering.exam_registration.model.Admin;
import com.softwareengineering.exam_registration.repository.AdminRepository;
import com.softwareengineering.exam_registration.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin getByAdminId(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() ->
                        new AdminNotFoundException("Admin not found"));
    }

    @Override
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(String id, Admin admin) {

        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new AdminNotFoundException("Admin not found"));

        existingAdmin.setAdminFirstName(admin.getAdminFirstName());
        existingAdmin.setAdminLastName(admin.getAdminLastName());
        existingAdmin.setAdminEmail(admin.getAdminEmail());
        existingAdmin.setAdminPhoneNo(admin.getAdminPhoneNo());
        existingAdmin.setAdminRole(admin.getAdminRole());
        existingAdmin.setAdminAddress(admin.getAdminAddress());

        return adminRepository.save(existingAdmin);
    }

    @Override
    public void deleteAdmin(String id) {

        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new AdminNotFoundException("Admin not found"));

        adminRepository.delete(existingAdmin);
    }
}