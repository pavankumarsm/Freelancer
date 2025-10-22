package org.example.freelancer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.example.freelancer.dto.FreelancerProfileDTO;
import org.example.freelancer.model.User;
import org.example.freelancer.service.FreelancerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/freelancers")
@RequiredArgsConstructor
@Tag(name = "Freelancer Management", description = "Manage freelancer profiles, skills, and ratings")
public class FreelancerController {

    private final FreelancerService freelancerService;

    @PutMapping("/{freelancerId}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long freelancerId,
                                              @RequestBody @Valid FreelancerProfileDTO profileDTO) {
        return ResponseEntity.ok(freelancerService.updateProfile(freelancerId, profileDTO));
    }
    @PutMapping("/{freelancerId}/deactivate")
    public ResponseEntity<String> deactivateAccount(@PathVariable Long freelancerId) {
        freelancerService.deactivateAccount(freelancerId);
        return ResponseEntity.ok("Freelancer account deactivated successfully.");
    }
}

/*
1. F: View Profile (/profile/{id}) – others can view freelancer details.
2. config:
You already have isActive flag in User.
When a freelancer deactivates → isActive = false.
If we don’t add any checks in login flow, Spring Security will still let them log in (since username & password match).
x That means deactivated users can still log in → not desired.
 */