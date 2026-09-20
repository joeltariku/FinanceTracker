package com.joeltariku.financetracker.auth.controller;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.auth.dto.AuthResultDto;
import com.joeltariku.financetracker.auth.dto.SignUpRequestDto;
import com.joeltariku.financetracker.auth.mapper.AuthMapper;
import com.joeltariku.financetracker.auth.service.AuthService;
import com.joeltariku.financetracker.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthMapper authMapper; // your external mapper

    public AuthController(AuthService authService, AuthMapper authMapper) {
        this.authService = authService;
        this.authMapper = authMapper;
    }

    @PostMapping("/signup")
    public AuthResultDto signup(@RequestBody @Valid SignUpRequestDto dto) {
        SignupRequest request = authMapper.toSignUpRequest(dto);
        AuthResult result = authService.signup(request);
        return authMapper.toAuthResultDTO(result);
    }
}
