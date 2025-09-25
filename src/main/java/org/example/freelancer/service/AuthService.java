package org.example.freelancer.service;

import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.model.User;

public interface AuthService {
    User registerUser(UserSignupDTO signupDTO);
}