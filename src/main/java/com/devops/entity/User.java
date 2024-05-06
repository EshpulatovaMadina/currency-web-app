package com.devops.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("user_account")
public class User  {
    @Id
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

}
