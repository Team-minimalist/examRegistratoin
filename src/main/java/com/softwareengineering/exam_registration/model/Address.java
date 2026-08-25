package com.softwareengineering.exam_registration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address {

    private String addressLine;

    private String city;

    private String district;

    private String state;

    private String pincode;

    private String country;
}