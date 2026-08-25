package com.softwareengineering.exam_registration.repository;

public interface CounterRepository {

    long getNextSequence(String counterName);
    
} 