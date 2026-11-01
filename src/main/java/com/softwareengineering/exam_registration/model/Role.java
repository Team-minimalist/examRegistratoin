package com.softwareengineering.exam_registration.model;

public enum Role {
    ROLE_STUDENT,
    ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }
}
