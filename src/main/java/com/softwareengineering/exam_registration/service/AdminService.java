package com.softwareengineering.exam_registration.service;

import com.softwareengineering.exam_registration.dto.AdminRegistrationRequest;
import com.softwareengineering.exam_registration.dto.AdminResponse;
import com.softwareengineering.exam_registration.dto.AdminStatsResponse;
import com.softwareengineering.exam_registration.model.Admin;

public interface AdminService {

    AdminResponse registerAdmin(AdminRegistrationRequest request);

    AdminResponse getAdminProfile(String usernameOrEmail);

    Admin getAdminByUsernameOrEmail(String usernameOrEmail);

    AdminStatsResponse getDashboardStats();

    void bootstrapInitialAdmin(String email, String password, String name, String username);
}
