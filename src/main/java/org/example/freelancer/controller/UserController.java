package org.example.freelancer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.freelancer.dto.ClientProfileDTO;
import org.example.freelancer.dto.FreelancerProfileDTO;
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

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody User user,
                                            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User currentUser = userService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(userService.createAdmin(user, currentUser));
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid UserSignupDTO signupDTO) {
        return ResponseEntity.ok(userService.registerUser(signupDTO));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long userId,
                                              @RequestBody @Valid FreelancerProfileDTO profileDTO) {
        return ResponseEntity.ok(userService.updateProfile(userId, profileDTO));
    }

    @PutMapping("/{clientId}/client-profile")
    public ResponseEntity<User> updateClientProfile(@PathVariable Long clientId,
                                                    @RequestBody @Valid ClientProfileDTO profileDTO) {
        return ResponseEntity.ok(userService.updateClientProfile(clientId, profileDTO));
    }
}
