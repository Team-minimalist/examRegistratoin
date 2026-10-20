package com.softwareengineering.exam_registration.service;

import java.util.List;

import com.softwareengineering.exam_registration.model.Admin;
public interface AdminService {

    Admin getByAdminId(String id);

    List<Admin> getAllAdmin();

    Admin updateAdmin(String id, Admin admin);

    void deleteAdmin(String id);

    Admin createAdmin(Admin admin);
}