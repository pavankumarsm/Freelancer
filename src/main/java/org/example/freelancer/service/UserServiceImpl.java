package org.example.freelancer.service;

import lombok.RequiredArgsConstructor;
import org.example.freelancer.constant.Role;
import org.example.freelancer.dto.ClientProfileDTO;
import org.example.freelancer.dto.FreelancerProfileDTO;
import org.example.freelancer.dto.UserSignupDTO;
import org.example.freelancer.exception.UnauthorizedException;
import org.example.freelancer.model.Client;
import org.example.freelancer.model.Freelancer;
import org.example.freelancer.model.User;
import org.example.freelancer.repository.ClientRepository;
import org.example.freelancer.repository.FreelancerRepository;
import org.example.freelancer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FreelancerRepository freelancerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createAdmin(User user, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admin can create another admin");
        }
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User registerUser(UserSignupDTO signupDTO) {
        if (signupDTO.getRole() == Role.ADMIN) {
            throw new UnauthorizedException("Cannot register as ADMIN directly");
        }

        User user;
        if (signupDTO.getRole() == Role.FREELANCER) {
            user = new Freelancer();
        } else {
            user = new Client();
        }

        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRole(signupDTO.getRole());
        return userRepository.save(user);
    }

    @Override
    public User updateProfile(Long userId, FreelancerProfileDTO profileDTO) {
        Freelancer freelancer = freelancerRepository.findById(userId)
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
    public User updateClientProfile(Long clientId, ClientProfileDTO profileDTO) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (profileDTO.getCompanyName() != null) client.setCompanyName(profileDTO.getCompanyName());
        if (profileDTO.getIndustry() != null) client.setIndustry(profileDTO.getIndustry());
        if (profileDTO.getAddress() != null) client.setAddress(profileDTO.getAddress());

        return clientRepository.save(client);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
