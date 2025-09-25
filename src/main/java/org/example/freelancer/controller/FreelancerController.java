package org.example.freelancer.controller;

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
public class FreelancerController {

    private final FreelancerService freelancerService;

    @PutMapping("/{freelancerId}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long freelancerId,
                                              @RequestBody @Valid FreelancerProfileDTO profileDTO) {
        return ResponseEntity.ok(freelancerService.updateProfile(freelancerId, profileDTO));
    }
}
