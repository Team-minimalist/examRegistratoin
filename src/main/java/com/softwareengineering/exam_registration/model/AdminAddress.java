package com.softwareengineering.exam_registration.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * AdminAddress
 */

@Getter 
@Setter 
public class AdminAddress {
    @NotBlank
    private String pincode;
    @NotBlank
    private String address;
    @NotBlank
    private String state;
    @NotBlank
    private String city;
}
