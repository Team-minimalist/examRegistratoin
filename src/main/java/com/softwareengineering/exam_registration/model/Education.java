package com.softwareengineering.exam_registration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Education {

    private String type;

    private String institutionName;

    private String courseOrClass;

    private String rollNumber;
}