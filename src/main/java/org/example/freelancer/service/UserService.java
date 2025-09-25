package org.example.freelancer.service;

import org.example.freelancer.model.User;

public interface UserService {
    User createAdmin(User user, User adminUser);
    User findByEmail(String email);
}
