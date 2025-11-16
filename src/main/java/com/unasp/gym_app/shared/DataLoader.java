package com.unasp.gym_app.shared;

import com.unasp.gym_app.infrastructure.entities.User;
import com.unasp.gym_app.infrastructure.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "123456"; // troque para algo seguro antes do deploy
            if (!userRepository.existsByUsername(adminUsername)) {
                var admin = User.builder()
                        .username(adminUsername)
                        .passwordHash(passwordEncoder.encode(adminPassword))
                        .role("ROLE_ADMIN")
                        .createdAt(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
                System.out.println("Admin user created: " + adminUsername + " / " + adminPassword);
            }
        };
    }
}
