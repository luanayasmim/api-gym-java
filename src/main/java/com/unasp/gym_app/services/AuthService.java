package com.unasp.gym_app.services;

import com.unasp.gym_app.infrastructure.entities.User;
import com.unasp.gym_app.infrastructure.repositories.UserRepository;
import com.unasp.gym_app.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String username, String password) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }

        var user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return null;
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    public User register(String username, String rawPassword, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        var user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }
}
