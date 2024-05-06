package com.devops.controller;

import com.devops.DTOS.SignUpDto;
import com.devops.DTOS.UserResponseDto;
import com.devops.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userservice;
    @PostMapping("/sign-up")
    public Mono<UserResponseDto> signUp(@RequestBody SignUpDto signUpDto) {
        return userservice.signUp(signUpDto);
    }

    @PostMapping("/sign-in")
    public Mono<UserResponseDto> signIn(@RequestParam String email, @RequestParam String password) {
        return userservice.signIn(email,password);
    }

}
