package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.*;
import org.example.freelancer.exception.UnauthorizedException;
import org.example.freelancer.model.Client;
import org.example.freelancer.model.Freelancer;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.AdminRepository;
import org.example.freelancer.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmailService emailService;

    @Override
    public User registerUser(UserSignupDTO signupDTO) {
        if (signupDTO.getRole() == Role.ADMIN) {
            throw new UnauthorizedException("Cannot register as ADMIN directly");
        }

        // Check if user exists
        Optional<User> existingUserOpt = adminRepository.findByEmail(signupDTO.getEmail());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if (!existingUser.isActive()) {
                existingUser.setActive(true);
                existingUser.setName(signupDTO.getName());
                existingUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
                existingUser.setRole(signupDTO.getRole());
                return adminRepository.save(existingUser);
            } else {
                throw new RuntimeException("Email already registered. Please login.");
            }
        }

        // New user
        User user = signupDTO.getRole() == Role.FREELANCER ? new Freelancer() : new Client();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRole(signupDTO.getRole());

        // Email verification setup
        String verificationToken = java.util.UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerified(false);
        user.setActive(true);

        User savedUser = adminRepository.save(user);

        // Send verification email
        emailService.sendEmail(
                savedUser.getEmail(),
                "Email Verification",
                "Click the link to verify your account: http://localhost:8080/api/auth/verify-email?email="
                        + savedUser.getEmail() + "&token=" + verificationToken
        );

        return savedUser;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        User user = adminRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid Password");
        }

        if (!user.isVerified()) {
            throw new UnauthorizedException("Please verify your email before login.");
        }

        if (user.isTwoFactorEnabled()) {
            // Generate OTP (6-digit)
            String otp = String.valueOf(100000 + new Random().nextInt(900000));

            user.setTwoFactorOtp(otp);
            user.setTwoFactorExpiry(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 minutes expiry
            adminRepository.save(user);

            // Send email
            emailService.sendEmail(
                    user.getEmail(),
                    "Your 2FA Verification Code",
                    "Your OTP code is: " + otp + "\nIt will expire in 5 minutes."
            );

            LoginResponseDTO response = new LoginResponseDTO();
            response.setTwoFactorRequired(true);
            return response;

        }
        String accessToken = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTwoFactorRequired(false);
        return response;
    }

    @Override
    public void logout(String token) {
        String email = jwtUtils.getEmailFromToken(token.replace("Bearer ", ""));
        adminRepository.findByEmail(email).ifPresent(user -> {
            // If you maintain a blacklist, add token there
        });
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        String email = jwtUtils.getEmailFromToken(refreshToken);
        User user = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtUtils.generateToken(user.getEmail());
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = java.util.UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        adminRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset",
                "Click to reset your password: http://localhost:8080/api/auth/reset-password?token=" + resetToken
        );
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        User user = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!dto.getToken().equals(user.getResetToken())) {
            throw new UnauthorizedException("Invalid reset token");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setResetToken(null);
        adminRepository.save(user);
    }

    @Override
    public void verifyEmail(EmailVerificationDTO dto) {
        User user = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!dto.getVerificationToken().equals(user.getVerificationToken())) {
            throw new UnauthorizedException("Invalid verification token");
        }

        user.setVerified(true);
        user.setVerificationToken(null); // clear token after verification
        adminRepository.save(user);
    }

    @Override
    public void enableTwoFactor(String userId) {
        User user = adminRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new UnauthorizedException("Email must be verified before enabling 2FA");
        }

        user.setTwoFactorEnabled(true);
        adminRepository.save(user);

        // send confirmation email
        emailService.sendEmail(
                user.getEmail(),
                "2FA Enabled",
                "Two-Factor Authentication has been enabled for your account."
        );
    }


    @Override
    public void verifyTwoFactor(TwoFactorDTO dto) {
        User user = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isTwoFactorEnabled()) {
            throw new UnauthorizedException("2FA is not enabled for this user");
        }

        // Check OTP
        if (user.getTwoFactorOtp() == null
                || !user.getTwoFactorOtp().equals(dto.getOtp())
                || user.getTwoFactorExpiry() == null
                || System.currentTimeMillis() > user.getTwoFactorExpiry()) {
            throw new UnauthorizedException("Invalid or expired OTP");
        }

        // OTP verified → clear OTP
        user.setTwoFactorOtp(null);
        user.setTwoFactorExpiry(null);
        adminRepository.save(user);

        // Issue JWT tokens after successful 2FA
        String accessToken = jwtUtils.generateToken(user.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

    }
}


