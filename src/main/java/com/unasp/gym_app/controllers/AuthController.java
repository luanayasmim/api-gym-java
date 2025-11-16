package com.unasp.gym_app.controllers;

import com.unasp.gym_app.services.AuthService;
import com.unasp.gym_app.shared.dtos.AuthResponse;
import com.unasp.gym_app.shared.dtos.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var token = authService.authenticate(request.getUsername(), request.getPassword());
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        // Note: read expiration from properties is not directly available here; we can return standard value
        return ResponseEntity.ok(new AuthResponse(token, 28800000L));
    }
}
