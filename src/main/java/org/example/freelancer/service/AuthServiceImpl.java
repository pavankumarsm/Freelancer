package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.exception.UnauthorizedException;
import org.example.freelancer.model.Client;
import org.example.freelancer.model.Freelancer;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserSignupDTO signupDTO) {
        if (signupDTO.getRole() == Role.ADMIN) {
            throw new UnauthorizedException("Cannot register as ADMIN directly");
        }

        // Check if user exists by email
        Optional<User> existingUserOpt = userRepository.findByEmail(signupDTO.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (!existingUser.isActive()) {
                // Reactivate account
                existingUser.setActive(true);
                existingUser.setName(signupDTO.getName());
                existingUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
                existingUser.setRole(signupDTO.getRole());
                return userRepository.save(existingUser);
            } else {
                throw new RuntimeException("Email already registered. Please login.");
            }
        }
        // Fresh user
        User user = signupDTO.getRole() == Role.FREELANCER ? new Freelancer() : new Client();

        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRole(signupDTO.getRole());

        return userRepository.save(user);
    }
}

