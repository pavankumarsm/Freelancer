package org.example.freelancer.service;

import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.UserResponseDTO;
import org.example.freelancer.model.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Map;

public interface AdminService {
    User createAdmin(User user, User adminUser);
    User findByEmail(String email);
    Page<UserResponseDTO> getAllUsers(int page, int size, Role role);
    User getUserById(Long id);
    User resetPassword(Long id, String newPassword);
    Page<UserResponseDTO> getNewUsers(LocalDate date, int page, int size);
    Page<UserResponseDTO> searchUsers(String keyword, int page, int size);
    Long getTotalUsersCount();
    Map<String, Long> getActiveStats();
    Map<String, Long> getRegistrations(String period);
    Map<String, Long> getRoleStats();
    User setActiveStatus(Long id, boolean isActive);
    User updateUser(Long id, User updatedData);
}