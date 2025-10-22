package org.example.freelancer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management", description = "APIs for user registration, authentication, and profile operations")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User Signup", description = "Register a new user with role, email, and password")
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid UserSignupDTO signupDTO) {
        return ResponseEntity.ok(authService.registerUser(signupDTO));
    }

    @Operation(summary = "User Login", description = "Authenticate a user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @Operation(summary = "Logout User", description = "Invalidate the user's JWT token and logout")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @Operation(summary = "Refresh Token", description = "Generate a new access token using a valid refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody String dto) {
        return ResponseEntity.ok(authService.refreshAccessToken(dto));
    }

    @Operation(summary = "Forgot Password", description = "Send a password reset link or OTP to the user's registered email")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset link/OTP sent to email.");
    }

    @Operation(summary = "Reset Password", description = "Reset the user password using reset token or OTP")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successful.");
    }

    @Operation(summary = "Verify Email", description = "Verify user email after signup using verification token")
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String token) {
        EmailVerificationDTO dto = new EmailVerificationDTO();
        dto.setEmail(email);
        dto.setVerificationToken(token);
        authService.verifyEmail(dto);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @Operation(summary = "Enable 2FA", description = "Enable two-factor authentication for a user")
    @PostMapping("/2fa/enable")
    public ResponseEntity<String> enable2FA(@RequestParam String userId) {
        authService.enableTwoFactor(userId);
        return ResponseEntity.ok("2FA enabled successfully.");
    }

    @Operation(summary = "Verify 2FA Code", description = "Verify a user's two-factor authentication code during login")
    @PostMapping("/2fa/verify")
    public ResponseEntity<String> verify2FA(@RequestBody @Valid TwoFactorDTO dto) {
        authService.verifyTwoFactor(dto);
        return ResponseEntity.ok("2FA verified successfully.");
    }
}
