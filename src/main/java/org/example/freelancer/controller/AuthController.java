package org.example.freelancer.controller;

import lombok.RequiredArgsConstructor;

import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.model.User;
import org.example.freelancer.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid UserSignupDTO signupDTO) {
        return ResponseEntity.ok(authService.registerUser(signupDTO));
    }

    // TODO: add login, refresh-token, logout later
}
/*
F: Login & Logout (/login, /logout) – secure session handling.
F: Forgot Password / Reset Password – OTP or email-based reset.
F: Email Verification – send verification link after signup.
F: 2FA (Two-Factor Authentication) – optional security feature.
 */