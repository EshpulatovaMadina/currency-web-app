package com.devops.DTOS;

import com.devops.entity.UserRole;
import lombok.*;

@Builder
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    public UserResponseDto(Long id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
