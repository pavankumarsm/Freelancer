package org.example.freelancer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Client Management", description = "Manage client details and account information")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Update Client Profile", description = "Update client account details like company info and contact details")
    @PutMapping("/{clientId}/profile")
    public ResponseEntity<User> updateClientProfile(@PathVariable Long clientId,
                                                    @RequestBody @Valid ClientProfileDTO profileDTO) {
        return ResponseEntity.ok(clientService.updateClientProfile(clientId, profileDTO));
    }

    @Operation(summary = "Deactivate Client Account", description = "Temporarily deactivate a client account (sets isActive=false)")
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
