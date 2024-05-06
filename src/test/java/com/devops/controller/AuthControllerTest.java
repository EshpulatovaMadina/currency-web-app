package com.devops.controller;

import com.devops.DTOS.SignUpDto;
import com.devops.DTOS.UserResponseDto;
import com.devops.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    AuthController authController;
    @Autowired
    private WebTestClient webTestClient;


    @Test
    void testSignUpEndpoint() {
        SignUpDto signUpDto = new SignUpDto("testuser", "password", "test@example.com");

        UserResponseDto responseDto = new UserResponseDto(1L, "testuser", "test@example.com", "password", "USER");

        when(userService.signUp(any(SignUpDto.class))).thenReturn(Mono.just(responseDto));

        webTestClient.post().uri("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signUpDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .isEqualTo(responseDto);
    }

    @Test
    void testSignInEndpoint() {
        UserResponseDto responseDto = new UserResponseDto(1L, "testuser", "test@example.com", "password", "USER");
        when(userService.signIn(any(),any())).thenReturn(Mono.just(responseDto));
        webTestClient.post().uri("/auth/sign-in?email=test@example.com&password=password")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .isEqualTo(responseDto);

    }


}
