package com.warung.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warung.inventory.dto.request.LoginRequest;
import com.warung.inventory.dto.request.RegisterRequest;
import com.warung.inventory.dto.response.ApiResponse;
import com.warung.inventory.dto.response.AuthResponse;
import com.warung.inventory.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest request) {
        AuthResponse register = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Registrasi berhasil")
                .data(register)
                .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse login = authService.login(request);

        return ResponseEntity.ok(
            ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login berhasil")
                .data(login)
                .build()
        );
    }
}
