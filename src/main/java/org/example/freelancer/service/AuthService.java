package org.example.freelancer.service;

import org.example.freelancer.dto.*;
import org.example.freelancer.model.User;

public interface AuthService {
    User registerUser(UserSignupDTO signupDTO);
    LoginResponseDTO login(LoginRequestDTO loginDTO);
    void logout(String token);
    String refreshAccessToken(String refreshToken);
    void initiatePasswordReset(String email);
    void resetPassword(ResetPasswordDTO dto);
    void verifyEmail(EmailVerificationDTO token);
    void enableTwoFactor(String userId);
    void verifyTwoFactor(TwoFactorDTO dto);
}