package com.softwareengineering.exam_registration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentRegistrationRequest {

    private String name;
    private String email;
    private String phone;
    private String password;
}
