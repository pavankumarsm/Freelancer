package org.example.freelancer.repository;

import org.example.freelancer.model.User;
import org.example.freelancer.constant.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

    Long countByIsActive(boolean isActive);

    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Long countByRole(Role role);
}