package com.devops.controller;

import com.devops.DTOS.SignUpDto;
import com.devops.DTOS.UserResponseDto;
import com.devops.entity.User;
import com.devops.enumaration.CurrencyStrategyName;
import com.devops.repository.UserRepository;
import com.devops.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private static final String USERNAME = "testuser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";

    @AfterEach
    public void setUp() {
        userRepository.deleteByEmail(EMAIL).block();
    }

    @Test
    void testSignUpEndpoint() {
        SignUpDto signUpDto = new SignUpDto(USERNAME,PASSWORD,EMAIL);

        webTestClient.post().uri("/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signUpDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .value(userResponseDto -> Objects.equals(userResponseDto.getEmail(),EMAIL));
    }

    @Test
    void testSignInEndpoint() {
        saveFakeUser();
        UserResponseDto responseDto = new UserResponseDto(1L, USERNAME, EMAIL, PASSWORD, "USER");
        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/auth/sign-in")
                                .queryParam("password",PASSWORD)
                                .queryParam("email",EMAIL)
                                .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .value(userResponseDto -> Objects.equals(userResponseDto.getEmail(),responseDto.getEmail()));
    }

    private SignUpDto createSignUpDTO(User user) {
        return new SignUpDto(user.getUsername(), user.getPassword(), user.getEmail());
    }

    private User saveFakeUser() {
        User user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .role("USER")
                .build();

        return userRepository.save(user).block();
    }
}
