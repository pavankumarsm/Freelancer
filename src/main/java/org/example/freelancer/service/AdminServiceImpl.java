package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.UserResponseDTO;
import org.example.freelancer.exception.UnauthorizedException;
import org.example.freelancer.exception.UserNotFoundException;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createAdmin(User user, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admin can create another admin");
        }
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return adminRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(int page, int size, Role role) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> users = (role == null) ?
                adminRepository.findAll(pageable) :
                adminRepository.findByRole(role, pageable);
        return users.map(UserResponseDTO::fromEntity);
    }

    @Override
    public User getUserById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public User resetPassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return adminRepository.save(user);
    }

    @Override
    public Page<UserResponseDTO> getNewUsers(LocalDate date, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return adminRepository.findByCreatedAtBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                pageable
        ).map(UserResponseDTO::fromEntity);
    }

    @Override
    public Page<UserResponseDTO> searchUsers(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return adminRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, pageable
        ).map(UserResponseDTO::fromEntity);
    }

    @Override
    public Long getTotalUsersCount() {
        return adminRepository.count();
    }

    @Override
    public Map<String, Long> getActiveStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("active", adminRepository.countByIsActive(true));
        stats.put("inactive", adminRepository.countByIsActive(false));
        return stats;
    }

    @Override
    public Map<String, Long> getRegistrations(String period) {
        Map<String, Long> stats = new HashMap<>();
        LocalDate today = LocalDate.now();

        if ("daily".equalsIgnoreCase(period)) {
            stats.put("today", adminRepository.countByCreatedAtBetween(
                    today.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            ));
        } else if ("weekly".equalsIgnoreCase(period)) {
            LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
            stats.put("thisWeek", adminRepository.countByCreatedAtBetween(
                    weekStart.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            ));
        } else if ("monthly".equalsIgnoreCase(period)) {
            LocalDate firstDay = today.withDayOfMonth(1);
            stats.put("thisMonth", adminRepository.countByCreatedAtBetween(
                    firstDay.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            ));
        } else if ("yearly".equalsIgnoreCase(period)) {
            LocalDate firstDay = today.withDayOfYear(1);
            stats.put("thisYear", adminRepository.countByCreatedAtBetween(
                    firstDay.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            ));
        }

        return stats;
    }

    @Override
    public Map<String, Long> getRoleStats() {
        Map<String, Long> stats = new HashMap<>();
        for (Role role : Role.values()) {
            stats.put(role.name().toLowerCase(), adminRepository.countByRole(role));
        }
        return stats;
    }

    @Override
    public User setActiveStatus(Long id, boolean isActive) {
        User user = getUserById(id);
        user.setActive(isActive);
        return adminRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedData) {
        User user = getUserById(id);
        user.setName(updatedData.getName());
        user.setEmail(updatedData.getEmail());
        // Skip updating role/password directly
        return adminRepository.save(user);
    }
}
