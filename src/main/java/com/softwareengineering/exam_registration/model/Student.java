package com.softwareengineering.exam_registration.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private StudentAcademicDetails academicDetails;

    private StudentAddress studentAddress;
}