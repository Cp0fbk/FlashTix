package com.flashtix.common.utils;

import com.flashtix.common.enums.Role;
import com.flashtix.entity.User;
import com.flashtix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminAccount();
    }

    private void initializeAdminAccount() {
        String adminEmail = "admin@flashtix.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Admin account created successfully");
            log.info("Email: {}", adminEmail);
            log.info("Password: Admin@123");
            log.info("Please change the password after first login");
        } else {
            log.info("Admin account already exists");
        }
    }
}
