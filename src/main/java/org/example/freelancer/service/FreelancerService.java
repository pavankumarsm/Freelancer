package org.example.freelancer.service;

import org.example.freelancer.dto.FreelancerProfileDTO;
import org.example.freelancer.model.User;

public interface FreelancerService {
    User updateProfile(Long freelancerId, FreelancerProfileDTO profileDTO);
    void deactivateAccount(Long freelancerId);
}
