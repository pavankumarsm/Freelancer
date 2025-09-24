package org.example.freelancer.service;

import org.example.freelancer.dto.ClientProfileDTO;
import org.example.freelancer.dto.FreelancerProfileDTO;
import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.model.User;

public interface UserService {
    User createAdmin(User user, User adminUser);
    User registerUser(UserSignupDTO signupDTO);    // minimal signup
    User updateProfile(Long userId, FreelancerProfileDTO profileDTO);
    User findByEmail(String email);
    User updateClientProfile(Long clientId, ClientProfileDTO profileDTO);
}
