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

        User user = signupDTO.getRole() == Role.FREELANCER ? new Freelancer() : new Client();

        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRole(signupDTO.getRole());

        return userRepository.save(user);
    }
}

