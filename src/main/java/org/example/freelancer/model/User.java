package org.example.freelancer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.freelancer.constant.Language;
import org.example.freelancer.constant.Role;

import java.util.HashSet;
import java.util.Set;


import java.math.BigDecimal;

/**
 * Represents a user in the freelancing platform.
 */
@Entity

@Table(name = "users",
        indexes = @Index(columnList = "email", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    private org.example.freelancer.constant.Role role;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;

    private String address;
    private String city;

    @Column(name = "profile_picture")
    private String profilePicture;

    private String resume;

    @Column(length = 1000)
    private String skills;  // Consider making this a separate entity

    @Column(length = 2000)
    private String bio;

    private String experience;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    // Consider adding these common fields
    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "is_active")
    private boolean isActive = true;
}

