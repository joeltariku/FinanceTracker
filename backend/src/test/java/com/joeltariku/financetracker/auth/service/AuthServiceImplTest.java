package com.joeltariku.financetracker.auth.service;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.exception.EmailAlreadyExistsException;
import com.joeltariku.financetracker.exception.UsernameAlreadyExistsException;
import com.joeltariku.financetracker.user.CreateUserRequest;
import com.joeltariku.financetracker.user.User;
import com.joeltariku.financetracker.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private SignupRequest request;

    @BeforeEach
    void setUp() {
        request = new SignupRequest("Joel", "Tariku", "wavyjoel", "joel@example.com", "secret123");
    }

    private User userWithId(UUID id) {
        User user = User.builder()
                .firstName("Joel")
                .lastName("Tariku")
                .username("wavyjoel")
                .email("joel@example.com")
                .passwordHash("hashed-password")
                .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Test
    void signup_validRequest_createsUserAndReturnsToken() {
        UUID id = UUID.randomUUID();
        User savedUser = userWithId(id);

        when(userService.findByEmail("joel@example.com")).thenReturn(Optional.empty());
        when(userService.findByUsername("wavyjoel")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-password");
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(savedUser);
        when(tokenService.generateToken(id.toString())).thenReturn("jwt-token");

        AuthResult result = authService.signup(request);

        assertSame(savedUser, result.user());
        assertEquals("jwt-token", result.token());
    }

    @Test
    void signup_validRequest_storesHashedPasswordNotRawPassword() {
        User savedUser = userWithId(UUID.randomUUID());

        when(userService.findByEmail(any())).thenReturn(Optional.empty());
        when(userService.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-password");
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(savedUser);
        when(tokenService.generateToken(any())).thenReturn("jwt-token");

        authService.signup(request);

        ArgumentCaptor<CreateUserRequest> captor = ArgumentCaptor.forClass(CreateUserRequest.class);
        verify(userService).createUser(captor.capture());

        CreateUserRequest sent = captor.getValue();

        assertEquals("hashed-password", sent.passwordHash());
        assertEquals("Joel", sent.firstName());
        assertEquals("Tariku", sent.lastName());
        assertEquals("wavyjoel", sent.username());
        assertEquals("joel@example.com", sent.email());
    }

    @Test
    void signup_duplicateEmail_throwsAndCreatesNothing() {
        when(userService.findByEmail("joel@example.com"))
                .thenReturn(Optional.of(userWithId(UUID.randomUUID())));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.signup(request));

        verify(userService, never()).createUser(any());
        verifyNoInteractions(passwordEncoder, tokenService);
    }

    @Test
    void signup_duplicateUsername_throwsAndCreatesNothing() {
        when(userService.findByEmail("joel@example.com")).thenReturn(Optional.empty());
        when(userService.findByUsername("wavyjoel"))
                .thenReturn(Optional.of(userWithId(UUID.randomUUID())));

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.signup(request));

        verify(userService, never()).createUser(any());
        verifyNoInteractions(passwordEncoder, tokenService);
    }
}
