package org.example.freelancer.controller;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.dto.PasswordUpdateDTO;
import org.example.freelancer.model.User;
import org.example.freelancer.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.example.freelancer.constant.Role;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody User user,
                                            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User currentUser = adminService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(adminService.createAdmin(user, currentUser));
    }
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Role role
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, role));
    }

    @PatchMapping("/users/{id}/reset-password")
    public ResponseEntity<User> resetPassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateDTO dto
    ) {
        return ResponseEntity.ok(adminService.resetPassword(id, dto.getNewPassword()));
    }

    @GetMapping("/clients")
    public ResponseEntity<Page<User>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, Role.CLIENT));
    }


    @GetMapping("/freelancers")
    public ResponseEntity<Page<User>> getAllFreelancers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size, Role.FREELANCER));
    }

    @GetMapping("/users/new")
    public ResponseEntity<Page<User>> getTodayNewUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.getNewUsers(LocalDate.now(), page, size));
    }

    @GetMapping("/users/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminService.searchUsers(keyword, page, size));
    }

    @GetMapping("/dashboard/users/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(adminService.getTotalUsersCount());
    }

    @GetMapping("/dashboard/users/active-stats")
    public ResponseEntity<?> getActiveStats() {
        return ResponseEntity.ok(adminService.getActiveStats());
    }


    @GetMapping("/dashboard/registrations")
    public ResponseEntity<?> getRegistrations(
            @RequestParam(defaultValue = "daily") String period
    ) {
        return ResponseEntity.ok(adminService.getRegistrations(period));
    }

}
