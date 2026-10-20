// package com.softwareengineering.exam_registration.config;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import com.softwareengineering.exam_registration.model.UserAddress;
// import com.softwareengineering.exam_registration.model.Student;
// import com.softwareengineering.exam_registration.repository.UserRepository;

// @Configuration 
// public class UserConfig {

//     @Bean
//     CommandLineRunner loadSampleData(UserRepository userRepository,PasswordEncoder passwordEncoder){
//         return args -> {
//             if (!userRepository.existsById("100")) {
                
//                 UserAddress address = new UserAddress();

//                 address.setPincode("735224");
//                 address.setAddress("maynaguri");
//                 address.setState("WB");
//                 address.setCity("maynaguri");
                
//                 Student users = new Student();
//                 users.setId("100");
//                 users.setUsername("Admin");
//                 users.setAddress(address);
//                 users.setNationalIdentification("Addhar -> 120000");
//                 users.setActive(true);
//                 users.setPassword(passwordEncoder.encode("1234@123"));
//                 userRepository.save(users);
//             }
//         };

//     }
// }
