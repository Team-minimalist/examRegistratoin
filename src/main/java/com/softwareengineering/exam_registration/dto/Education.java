package com.softwareengineering.exam_registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    @NotBlank(message = "Education type is required")
    private String type;

    @NotBlank(message = "Institute name is required")
    private String instituteName;

    @NotBlank(message = "Course or Class is required")
    private String courseOrClass;
    
    private String rollNumber;

}
