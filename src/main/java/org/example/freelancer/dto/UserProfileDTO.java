package org.example.freelancer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.example.freelancer.constant.Language;

import java.util.Set;
@Data
public class UserProfileDTO {

    private String profileImageUrl;
    private String phoneNumber;
    private String location;

    @Size(max = 1000)
    private String bio;

    private String skills;
    private Integer yearsOfExperience;
    private String websiteUrl;
    private String linkedInUrl;
    private String githubUrl;
    private Set<Language> languages;
}
