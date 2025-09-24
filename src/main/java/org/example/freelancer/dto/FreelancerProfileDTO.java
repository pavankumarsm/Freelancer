package org.example.freelancer.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FreelancerProfileDTO {
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;
    private String address;
    private String city;
    private String profilePicture;
    private String resume;
    private String skills;
    private String bio;
    private String experience;
}
