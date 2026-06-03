package com.warung.inventory.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.warung.inventory.dto.request.LoginRequest;
import com.warung.inventory.dto.request.RegisterRequest;
import com.warung.inventory.dto.response.AuthResponse;
import com.warung.inventory.model.Role;
import com.warung.inventory.model.User;
import com.warung.inventory.repository.UserRepository;
import com.warung.inventory.security.JwtService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        Optional<User> emailRegistered = userRepository.findByEmail(request.getEmail());

        if(emailRegistered.isPresent()) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        User user = User.builder()
                .nama(request.getNama())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        Optional<User> emailRegistered = userRepository.findByEmail(request.getEmail());

        if(emailRegistered.isEmpty()) {
            throw new RuntimeException("User tidak ditemukan");
        }

        User user = emailRegistered.get();
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
