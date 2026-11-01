package com.softwareengineering.exam_registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.UserRepository;

@SpringBootTest
class ExamRegistrationApplicationTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ExamRepository examRepository;

    @MockBean
    private ExamRegistrationRepository examRegistrationRepository;

    @Test
    void contextLoads() {
    }

}
