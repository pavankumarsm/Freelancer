package org.example.freelancer.controller;

import lombok.RequiredArgsConstructor;

import org.example.freelancer.dto.*;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody String dto) {
        return ResponseEntity.ok(authService.refreshAccessToken(dto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset link/OTP sent to email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successful.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String email,
            @RequestParam String token) {
        EmailVerificationDTO dto = new EmailVerificationDTO();
        dto.setEmail(email);
        dto.setVerificationToken(token);
        authService.verifyEmail(dto);
        return ResponseEntity.ok("Email verified successfully.");
    }


    @PostMapping("/2fa/enable")
    public ResponseEntity<String> enable2FA(@RequestParam String userId) {
        authService.enableTwoFactor(userId);
        return ResponseEntity.ok("2FA enabled successfully.");
    }
    @PostMapping("/2fa/verify")
    public ResponseEntity<String> verify2FA(@RequestBody @Valid TwoFactorDTO dto) {
        authService.verifyTwoFactor(dto);
        return ResponseEntity.ok("2FA verified successfully.");
    }
}
/*
F: Login & Logout (/login, /logout) – secure session handling.
F: Forgot Password / Reset Password – OTP or email-based reset.
F: Email Verification – send verification link after signup.
F: 2FA (Two-Factor Authentication) – optional security feature.
 */