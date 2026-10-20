package com.softwareengineering.exam_registration.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "exams")
public class Exam {

    @Id
    private String id;

    private String examName;

    private String description;

    private String examType;

    private LocalDateTime registrationStart;

    private LocalDateTime registrationEnd;

    private LocalDateTime examStart;

    private LocalDateTime examEnd;

    private Integer durationMinutes;

    private Integer totalMarks;

    private Integer passingMarks;

    private String status;
}