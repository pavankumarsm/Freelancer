package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.exception.UnauthorizedException;
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
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public Page<User> getAllUsers(int page, int size, Role role) {
        PageRequest pageable = PageRequest.of(page, size);
        if (role == null) {
            return adminRepository.findAll(pageable);
        }
        return adminRepository.findByRole(role, pageable);
    }

    @Override
    public User getUserById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User resetPassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return adminRepository.save(user);
    }

    @Override
    public Page<User> getNewUsers(LocalDate date, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return adminRepository.findByCreatedAtBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                pageable
        );
    }

    @Override
    public Page<User> searchUsers(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return adminRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
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
        } else if ("monthly".equalsIgnoreCase(period)) {
            LocalDate firstDay = today.withDayOfMonth(1);
            stats.put("thisMonth", adminRepository.countByCreatedAtBetween(
                    firstDay.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            ));
        }

        return stats;
    }
}
