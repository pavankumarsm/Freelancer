package org.example.freelancer.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private boolean twoFactorRequired;
}
