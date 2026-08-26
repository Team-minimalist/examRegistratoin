package com.softwareengineering.exam_registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identification {

    @NotBlank(message = "Identification type is required")
    private String type;

    @NotBlank(message = "Document ID is required")
    private String documentId;
    
}
