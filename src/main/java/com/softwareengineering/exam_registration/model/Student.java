package com.softwareengineering.exam_registration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    private String studentId;

    private PersonalInfo personalInfo;

    private Education education;

    private Identification identification;

    private ContactInfo contact;

    private Address address;

    private LocalDateTime createdAt;
}