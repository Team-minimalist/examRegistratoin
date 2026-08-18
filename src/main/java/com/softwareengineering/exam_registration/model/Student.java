package com.softwareengineering.exam_registration.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Students")
public class Student {

    @Id
    private String id;

    private String name;
    private String phone;
    private String email;
    private String password;

}
