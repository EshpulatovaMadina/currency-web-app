package com.devops.service;

import com.devops.DTOS.SignUpDto;
import com.devops.DTOS.UserResponseDto;
import com.devops.entity.User;
import com.devops.exceptions.DataNotFoundException;
import com.devops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.naming.AuthenticationException;

import static com.devops.entity.UserRole.USER_ROLE;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void signUp_NewUser_Success() {
        SignUpDto signUpDto = new SignUpDto("test@example.com","test_user","password");

        when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenAnswer(user -> Mono.just(user.getArgument(0)));

        Mono<UserResponseDto> result = userService.signUp(signUpDto);

        StepVerifier.create(result)
                .expectNextMatches(userResponseDto ->
                        userResponseDto.getUsername().equals(signUpDto.getUsername()) &&
                                userResponseDto.getPassword().equals(signUpDto.getPassword()) &&
                                userResponseDto.getRole().equals(USER_ROLE.name())
                )
                .verifyComplete();
    }


    @Test
    void signIn_UserFound_PasswordMatch() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(USER_ROLE.name());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Mono.just(user));

        Mono<UserResponseDto> result = userService.signIn("test@example.com", "password");

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.getId().equals(user.getId())
                            && response.getUsername().equals(user.getUsername())
                            && response.getEmail().equals(user.getEmail())
                            && response.getPassword().equals(user.getPassword()) // Note: You may want to reconsider returning password in the response
                            && response.getRole().equals(user.getRole());
                })
                .verifyComplete();

    }

    @Test
    void signIn_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());

        Mono<UserResponseDto> result = userService.signIn("test@example.com", "password");

        StepVerifier.create(result)
                .expectError(DataNotFoundException.class)
                .verify();
    }

    @Test
    void signIn_PasswordNotMatch() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Mono.just(user));

        Mono<UserResponseDto> result = userService.signIn("test@example.com", "password");

        StepVerifier.create(result)
                .expectError(AuthenticationException.class)
                .verify();
    }
}
