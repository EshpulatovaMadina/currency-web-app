package com.devops.DTOS;

import com.devops.entity.UserRole;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpDto {
    private String username;
    private String password;
    private String email;
}
