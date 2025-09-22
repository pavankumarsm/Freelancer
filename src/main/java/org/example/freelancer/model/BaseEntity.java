package org.example.freelancer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;
}
