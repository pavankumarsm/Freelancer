package org.example.freelancer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otpOrToken;

    @NotBlank
    private String newPassword;
}
