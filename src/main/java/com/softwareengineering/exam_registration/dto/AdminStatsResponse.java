package com.softwareengineering.exam_registration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalStudents;
    private long activeStudents;
    private long totalExams;
    private long activeExams;
    private long publishedExams;
    private long totalRegistrations;
    private long activeRegistrations;
    private long cancelledRegistrations;
}
