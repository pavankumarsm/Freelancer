package org.example.freelancer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.freelancer.constant.Role;

@Entity
@Table(name = "users", indexes = @Index(columnList = "email", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User extends BaseEntity {

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @Email
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "is_active")
    private boolean isActive = true;
}
