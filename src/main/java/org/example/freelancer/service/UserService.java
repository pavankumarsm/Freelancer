package org.example.freelancer.service;

import org.example.freelancer.dto.UserProfileDTO;
import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.model.User;

public interface UserService {
    User createAdmin(User user, User adminUser);
    User registerUser(UserSignupDTO signupDTO);    // minimal signup
    User updateProfile(Long userId, UserProfileDTO profileDTO);
    User findByEmail(String email);
}
