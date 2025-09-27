package org.example.freelancer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TwoFactorDTO {
    @Email
    private String email;

    @NotBlank
    private String otp;

    // Added so verifyTwoFactor can return JWTs
    private String accessToken;
    private String refreshToken;
}
