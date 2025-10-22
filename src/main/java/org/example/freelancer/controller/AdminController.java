package org.example.freelancer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.PasswordUpdateDTO;
import org.example.freelancer.dto.UserResponseDTO;
import org.example.freelancer.model.User;
import org.example.freelancer.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Admin Operations", description = "Administrative endpoints for managing users and system data")
public class AdminController {

    private final AdminService adminService;

    // Create new admin
    @PostMapping("/admin")
    public ResponseEntity<UserResponseDTO> createAdmin(
            @RequestBody User user,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        User currentUser = adminService.findByEmail(principal.getUsername());
        User createdAdmin = adminService.createAdmin(user, currentUser);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(createdAdmin));
    }

    // Get all users (optional role filter)
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Role role
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, role));
    }

    // Reset user password
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<UserResponseDTO> resetPassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateDTO dto
    ) {
        User updatedUser = adminService.resetPassword(id, dto.getNewPassword());
        return ResponseEntity.ok(UserResponseDTO.fromEntity(updatedUser));
    }

    // Get all clients
    @GetMapping("/clients")
    public ResponseEntity<Page<UserResponseDTO>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, Role.CLIENT));
    }

    // Get all freelancers
    @GetMapping("/freelancers")
    public ResponseEntity<Page<UserResponseDTO>> getAllFreelancers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, Role.FREELANCER));
    }

    // Get today's new users
    @GetMapping("/new")
    public ResponseEntity<Page<UserResponseDTO>> getTodayNewUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getNewUsers(LocalDate.now(), page, size));
    }

    // Search users
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.searchUsers(keyword, page, size));
    }

    // Total users count
    @GetMapping("/dashboard/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(adminService.getTotalUsersCount());
    }

    // Active/inactive stats
    @GetMapping("/dashboard/active-stats")
    public ResponseEntity<?> getActiveStats() {
        return ResponseEntity.ok(adminService.getActiveStats());
    }

    // Registrations stats by period (daily, weekly, monthly, yearly)
    @GetMapping("/dashboard/registrations")
    public ResponseEntity<?> getRegistrations(
            @RequestParam(defaultValue = "daily") String period
    ) {
        return ResponseEntity.ok(adminService.getRegistrations(period));
    }

    @GetMapping("/stats/roles")
    public Map<String, Long> getRoleStats() {
        return adminService.getRoleStats();
    }

    @PatchMapping("/users/{id}/status")
    public User setActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        return adminService.setActiveStatus(id, active);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return adminService.updateUser(id, updatedUser);
    }


}
