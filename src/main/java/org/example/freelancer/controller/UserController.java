package org.example.freelancer.controller;

import lombok.RequiredArgsConstructor;
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
}
