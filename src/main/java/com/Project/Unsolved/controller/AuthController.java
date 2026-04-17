package com.Project.Unsolved.controller;

import com.Project.Unsolved.dto.LoginRequestDto;
import com.Project.Unsolved.dto.SignUpRequestDto;
import com.Project.Unsolved.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ SIGNUP + AUTO LOGIN (RETURNS JWT)
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignUpRequestDto requestDto
    ) {
        String token = authService.signUpAndLogin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequestDto requestDto
    ) {
        String token = authService.login(requestDto);
        return ResponseEntity.ok(token);
    }
}
