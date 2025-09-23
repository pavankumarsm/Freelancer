package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.UserProfileDTO;
import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.exception.UnauthorizedException;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createAdmin(User user, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admin can create another admin");
        }
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRating(0.0);
        user.setLanguages(new HashSet<>());
        return userRepository.save(user);
    }

    @Override
    public User registerUser(UserSignupDTO signupDTO) {
        if (signupDTO.getRole() == Role.ADMIN) {
            throw new UnauthorizedException("Cannot register as ADMIN directly");
        }

        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRole(signupDTO.getRole());
        user.setRating(0.0);
        user.setLanguages(new HashSet<>());

        return userRepository.save(user);
    }

    @Override
    public User updateProfile(Long userId, UserProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (profileDTO.getProfileImageUrl() != null) user.setProfileImageUrl(profileDTO.getProfileImageUrl());
        if (profileDTO.getPhoneNumber() != null) user.setPhoneNumber(profileDTO.getPhoneNumber());
        if (profileDTO.getLocation() != null) user.setLocation(profileDTO.getLocation());
        if (profileDTO.getBio() != null) user.setBio(profileDTO.getBio());
        if (profileDTO.getSkills() != null) user.setSkills(profileDTO.getSkills());
        if (profileDTO.getYearsOfExperience() != null) user.setYearsOfExperience(profileDTO.getYearsOfExperience());
        if (profileDTO.getWebsiteUrl() != null) user.setWebsiteUrl(profileDTO.getWebsiteUrl());
        if (profileDTO.getLinkedInUrl() != null) user.setLinkedInUrl(profileDTO.getLinkedInUrl());
        if (profileDTO.getGithubUrl() != null) user.setGithubUrl(profileDTO.getGithubUrl());
        if (profileDTO.getLanguages() != null && !profileDTO.getLanguages().isEmpty())
            user.setLanguages(profileDTO.getLanguages());

        return userRepository.save(user);
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
