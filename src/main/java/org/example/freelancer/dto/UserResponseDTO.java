package org.example.freelancer.dto;

import lombok.Data;
import org.example.freelancer.constant.Role;
import org.example.freelancer.model.User;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean active;

    public static UserResponseDTO fromEntity(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }
}