package org.example.freelancer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "freelancers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("FREELANCER")
public class Freelancer extends User {

    @Size(max = 20)
    private String phone;

    private String address;
    private String city;

    @Column(name = "profile_picture")
    private String profilePicture;

    private String resume;

    @Column(length = 1000)
    private String skills;

    @Column(length = 2000)
    private String bio;

    private String experience;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;
}
