package com.joeltariku.financetracker.auth.controller;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.auth.dto.AuthResultDto;
import com.joeltariku.financetracker.auth.dto.SignUpRequestDto;
import com.joeltariku.financetracker.auth.mapper.AuthMapper;
import com.joeltariku.financetracker.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResultDto signup(@RequestBody @Valid SignUpRequestDto dto) {
        SignupRequest request = authMapper.toSignUpRequest(dto);
        AuthResult result = authService.signup(request);
        return authMapper.toAuthResultDTO(result);
    }
}
