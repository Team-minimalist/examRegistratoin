package com.softwareengineering.exam_registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import com.softwareengineering.exam_registration.repository.AdminRepository;
import com.softwareengineering.exam_registration.repository.ExamRegistrationRepository;
import com.softwareengineering.exam_registration.repository.ExamRepository;
import com.softwareengineering.exam_registration.repository.StudentRepository;

@SpringBootTest(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/test",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
})
class ExamRegistrationApplicationTests {

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private ExamRepository examRepository;

    @MockBean
    private ExamRegistrationRepository examRegistrationRepository;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
    }

}
