package com.configuration;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.entity.User;
import com.enums.Role;
import com.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

    	 if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

    	        User admin = new User();

    	        admin.setFirstName("System");
    	        admin.setLastName("Admin");
    	        admin.setEmail("admin@gmail.com");
    	        admin.setPassword(passwordEncoder.encode("Admin@123"));
    	        admin.setRole(Role.ADMIN);
    	        admin.setCreatedAt(LocalDateTime.now());
    	        admin.setUpdatedAt(LocalDateTime.now());
    	        admin.setActive(true);

    	        userRepository.save(admin);

    	        System.out.println("Admin created successfully.");
    	    }
    }
}
