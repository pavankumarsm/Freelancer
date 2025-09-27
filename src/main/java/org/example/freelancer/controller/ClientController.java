package org.example.freelancer.controller;

import lombok.RequiredArgsConstructor;

import org.example.freelancer.dto.ClientProfileDTO;
import org.example.freelancer.model.User;
import org.example.freelancer.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PutMapping("/{clientId}/profile")
    public ResponseEntity<User> updateClientProfile(@PathVariable Long clientId,
                                                    @RequestBody @Valid ClientProfileDTO profileDTO) {
        return ResponseEntity.ok(clientService.updateClientProfile(clientId, profileDTO));
    }
    @PutMapping("/{clientId}/deactivate")
    public ResponseEntity<String> deactivateClient(@PathVariable Long clientId) {
        clientService.deactivateClient(clientId);
        return ResponseEntity.ok("Client account deactivated successfully");
    }
}
/*
F: View Profile (company details visible to freelancers).
we need to work on
 */
