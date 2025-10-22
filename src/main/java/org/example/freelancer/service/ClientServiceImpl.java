package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.freelancer.dto.ClientProfileDTO;
import org.example.freelancer.model.Client;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public User updateClientProfile(Long clientId, ClientProfileDTO profileDTO) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (profileDTO.getCompanyName() != null) client.setCompanyName(profileDTO.getCompanyName());
        if (profileDTO.getIndustry() != null) client.setIndustry(profileDTO.getIndustry());
        if (profileDTO.getAddress() != null) client.setAddress(profileDTO.getAddress());

        return clientRepository.save(client);
    }
    @Override
    public User deactivateClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setActive(false);
        return clientRepository.save(client);
    }

}

