package org.example.freelancer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.freelancer.constant.Language;
import org.example.freelancer.constant.Role;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // ADMIN, CLIENT, FREELANCER_USER

    private String profileImageUrl;
    private String phoneNumber;
    private String location;

    @Column(length = 1000)
    private String bio;
    private String skills;
    @Min(0)
    private Integer yearsOfExperience;

    private String websiteUrl;
    private String linkedInUrl;
    private String githubUrl;


    @ElementCollection(targetClass = Language.class)
    @CollectionTable(name = "user_languages", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Language> languages = new HashSet<>();

    @Column(nullable = false)
    private Double rating = 0.0;
}

