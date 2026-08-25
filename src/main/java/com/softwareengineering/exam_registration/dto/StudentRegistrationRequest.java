package com.softwareengineering.exam_registration.dto;

import com.softwareengineering.exam_registration.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentRegistrationRequest {

    private PersonalInfo personalInfo;

    private Education education;

    private Identification identification;

    private ContactInfo contact;

    private Address address;
}