package org.example.freelancer.service;

import org.example.freelancer.dto.ClientProfileDTO;
import org.example.freelancer.model.User;

public interface ClientService {
    User updateClientProfile(Long clientId, ClientProfileDTO profileDTO);
}
