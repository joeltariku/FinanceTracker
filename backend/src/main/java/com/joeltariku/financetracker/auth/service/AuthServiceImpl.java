package com.joeltariku.financetracker.auth.service;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.exception.EmailAlreadyExistsException;
import com.joeltariku.financetracker.exception.UsernameAlreadyExistsException;
import com.joeltariku.financetracker.user.CreateUserRequest;
import com.joeltariku.financetracker.user.User;
import com.joeltariku.financetracker.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public AuthResult signup(SignupRequest request) {
        if (userService.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException(request.email());
        } else if (userService.findByUsername(request.username()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        String passwordHash = passwordEncoder.encode(request.password());

        CreateUserRequest createUserRequest = new CreateUserRequest(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                passwordHash
        );
        User user = userService.createUser(createUserRequest);

        String token = tokenService.generateToken(user.getId().toString());
        return new AuthResult(user, token);
    }
}
