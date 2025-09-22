package org.example.freelancer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.freelancer.dto.UserProfileDTO;
import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.model.User;
import org.example.freelancer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. Create Admin (restricted in real apps)
    @PostMapping("/admin")
    // Optional: use Spring Security PreAuthorize if configured
    public ResponseEntity<User> createAdmin(@RequestBody User user,
                                            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        // Fetch logged-in admin from DB
        User currentUser = userService.findByEmail(principal.getUsername());
        User createdAdmin = userService.createAdmin(user, currentUser);
        return ResponseEntity.ok(createdAdmin);
    }

    // 2️Signup (minimal fields)
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid UserSignupDTO signupDTO) {
        return ResponseEntity.ok(userService.registerUser(signupDTO));
    }

    // 3️update profile after signup
    @PutMapping("/{userId}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long userId,
                                              @RequestBody @Valid UserProfileDTO profileDTO) {
        return ResponseEntity.ok(userService.updateProfile(userId, profileDTO));
    }
}
