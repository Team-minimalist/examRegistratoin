package com.softwareengineering.exam_registration.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exam_registrations")
@CompoundIndex(name = "student_exam_unique_idx", def = "{'studentId': 1, 'examId': 1}", unique = true)
public class ExamRegistration {

    @Id
    private String id;

    @Indexed
    private String studentId;

    @Indexed
    private String examId;

    @Indexed(unique = true)
    private String registrationNumber;

    @CreatedDate
    private LocalDateTime registeredAt;

    @Builder.Default
    private RegistrationStatus status = RegistrationStatus.REGISTERED;

    @Builder.Default
    private String paymentStatus = "EXEMPT";

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
