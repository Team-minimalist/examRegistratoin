package com.softwareengineering.exam_registration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PersonalInfo {

    private String firstName;
    private String middleName;
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    private String fatherName;
    private String motherName;
}