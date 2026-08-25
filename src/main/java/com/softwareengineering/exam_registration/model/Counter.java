package com.softwareengineering.exam_registration.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "counter")
public class Counter {
    
    @Id
    private String id;
    private long sewuence;

}
