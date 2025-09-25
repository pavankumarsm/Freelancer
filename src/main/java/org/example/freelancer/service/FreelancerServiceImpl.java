package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.dto.FreelancerProfileDTO;
import org.example.freelancer.model.Freelancer;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.FreelancerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreelancerServiceImpl implements FreelancerService {

    private final FreelancerRepository freelancerRepository;

    @Override
    public User updateProfile(Long freelancerId, FreelancerProfileDTO profileDTO) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        if (profileDTO.getPhone() != null) freelancer.setPhone(profileDTO.getPhone());
        if (profileDTO.getAddress() != null) freelancer.setAddress(profileDTO.getAddress());
        if (profileDTO.getCity() != null) freelancer.setCity(profileDTO.getCity());
        if (profileDTO.getProfilePicture() != null) freelancer.setProfilePicture(profileDTO.getProfilePicture());
        if (profileDTO.getResume() != null) freelancer.setResume(profileDTO.getResume());
        if (profileDTO.getSkills() != null) freelancer.setSkills(profileDTO.getSkills());
        if (profileDTO.getBio() != null) freelancer.setBio(profileDTO.getBio());
        if (profileDTO.getExperience() != null) freelancer.setExperience(profileDTO.getExperience());

        return freelancerRepository.save(freelancer);
    }

    @Override
    public void deactivateAccount(Long freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found with id: " + freelancerId));

        freelancer.setActive(false);  // soft delete
        freelancerRepository.save(freelancer);
    }
}
