package org.example.freelancer.service;

import org.example.freelancer.constant.Role;
import org.example.freelancer.model.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Map;

public interface AdminService {
    User createAdmin(User user, User adminUser);
    User findByEmail(String email);
    Page<User> getAllUsers(int page, int size, Role role);
    User getUserById(Long id);
    User resetPassword(Long id, String newPassword);
    Page<User> getNewUsers(LocalDate date, int page, int size);
    Page<User> searchUsers(String keyword, int page, int size);
    Long getTotalUsersCount();
    Map<String, Long> getActiveStats();
    Map<String, Long> getRegistrations(String period);
}
