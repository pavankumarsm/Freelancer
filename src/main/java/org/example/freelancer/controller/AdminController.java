package org.example.freelancer.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create new admin", description = "Allows an existing admin to create another admin user")
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
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of users with an optional role filter")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Role role
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, role));
    }

    // Reset user password
    @PatchMapping("/{id}/reset-password")
    @Operation(summary = "Reset user password", description = "Reset the password for a specific user by ID")
    public ResponseEntity<UserResponseDTO> resetPassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateDTO dto
    ) {
        User updatedUser = adminService.resetPassword(id, dto.getNewPassword());
        return ResponseEntity.ok(UserResponseDTO.fromEntity(updatedUser));
    }

    // Get all clients
    @GetMapping("/clients")
    @Operation(summary = "Get all clients", description = "Retrieve a paginated list of all registered clients")
    public ResponseEntity<Page<UserResponseDTO>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, Role.CLIENT));
    }

    // Get all freelancers
    @GetMapping("/freelancers")
    @Operation(summary = "Get all freelancers", description = "Retrieve a paginated list of all registered freelancers")
    public ResponseEntity<Page<UserResponseDTO>> getAllFreelancers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, Role.FREELANCER));
    }

    // Get today's new users
    @GetMapping("/new")
    @Operation(summary = "Get today’s new users", description = "Retrieve all users who registered today")
    public ResponseEntity<Page<UserResponseDTO>> getTodayNewUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getNewUsers(LocalDate.now(), page, size));
    }

    // Search users
    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search for users by keyword (name, email, etc.)")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.searchUsers(keyword, page, size));
    }

    // Total users count
    @GetMapping("/dashboard/count")
    @Operation(summary = "Get total users count", description = "Retrieve the total number of registered users")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(adminService.getTotalUsersCount());
    }

    // Active/inactive stats
    @GetMapping("/dashboard/active-stats")
    @Operation(summary = "Get active/inactive user stats", description = "Retrieve statistics for active and inactive users")
    public ResponseEntity<?> getActiveStats() {
        return ResponseEntity.ok(adminService.getActiveStats());
    }

    // Registrations stats by period (daily, weekly, monthly, yearly)
    @GetMapping("/dashboard/registrations")
    @Operation(summary = "Get registration statistics", description = "Retrieve user registration trends (daily, weekly, monthly, yearly)")
    public ResponseEntity<?> getRegistrations(
            @RequestParam(defaultValue = "daily") String period
    ) {
        return ResponseEntity.ok(adminService.getRegistrations(period));
    }

    @GetMapping("/stats/roles")
    @Operation(summary = "Get user count by roles", description = "Retrieve total count of users by role type (Admin, Client, Freelancer)")
    public Map<String, Long> getRoleStats() {
        return adminService.getRoleStats();
    }

    @PatchMapping("/users/{id}/status")
    @Operation(summary = "Set user active status", description = "Activate or deactivate a specific user by ID")
    public User setActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        return adminService.setActiveStatus(id, active);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update user details", description = "Update user profile details such as name, email, and role")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return adminService.updateUser(id, updatedUser);
    }


}
