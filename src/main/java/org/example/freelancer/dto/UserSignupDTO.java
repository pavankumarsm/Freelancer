package org.example.freelancer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.freelancer.constant.Role;
@Data
public class UserSignupDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Role role; // CLIENT or FREELANCER
}
