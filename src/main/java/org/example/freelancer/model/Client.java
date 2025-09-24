package org.example.freelancer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CLIENT")
public class Client extends User {

    private String companyName;
    private String industry;
    private String address;

    @Column(name = "number_of_post")
    private int numberOfPost = 0;
}
