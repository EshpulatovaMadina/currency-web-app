package com.devops.service;

import com.devops.DTOS.SignUpDto;
import com.devops.DTOS.UserResponseDto;
import com.devops.entity.User;
import com.devops.entity.UserRole;
import com.devops.exceptions.DataAlreadyExistsException;
import com.devops.exceptions.DataNotFoundException;
import com.devops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserResponseDto> signUp(SignUpDto signUpDto) {
        User newUser = User.builder()
                .email(signUpDto.getEmail())
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .role(UserRole.USER_ROLE.name())
                .build();

        return userRepository.findByEmail(signUpDto.getEmail())
                .flatMap(user -> Mono.error(new DataAlreadyExistsException("User already exists with email: " + signUpDto.getEmail())))
                .then(userRepository.save(newUser))
                .map(savedUser -> new UserResponseDto(
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getEmail(),
                        savedUser.getPassword(),
                        savedUser.getRole()
                ));
    }

    public Mono<UserResponseDto> signIn(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new DataNotFoundException("User not found with email: " + email)))
                .flatMap(user -> {
                    if(user.getPassword().equals(password)) {
                        return Mono.just(new UserResponseDto(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRole()
                        ));
                    }else {
                        return Mono.error(new AuthenticationException("Password didn't match"));
                    }

                });
    }
}
