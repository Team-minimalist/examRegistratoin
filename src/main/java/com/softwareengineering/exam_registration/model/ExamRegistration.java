package com.softwareengineering.exam_registration.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "exam_registrations")
public class ExamRegistration {

    @Id
    private String id;

    private String studentId;

    private String examId;

    private LocalDateTime registrationDate;

    private String status;
}