package com.joeltariku.financetracker.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;
import com.joeltariku.financetracker.auth.dto.AuthResultDto;
import com.joeltariku.financetracker.auth.dto.SignUpRequestDto;
import com.joeltariku.financetracker.auth.mapper.AuthMapper;
import com.joeltariku.financetracker.auth.service.AuthService;
import com.joeltariku.financetracker.config.SecurityConfig;
import com.joeltariku.financetracker.exception.EmailAlreadyExistsException;
import com.joeltariku.financetracker.exception.UsernameAlreadyExistsException;
import com.joeltariku.financetracker.user.User;
import com.joeltariku.financetracker.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class   AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthMapper authMapper;

    private SignUpRequestDto validDto;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        validDto = new SignUpRequestDto("Joel", "Tariku", "wavyjoel", "joel@example.com", "secret123");
        signupRequest = new SignupRequest("Joel", "Tariku", "wavyjoel", "joel@example.com", "secret123");
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
    void signup_validRequest_returns201WithAuthResult() throws Exception {
        UUID id = UUID.randomUUID();
        AuthResult authResult = new AuthResult(userWithId(id), "jwt-token");
        AuthResultDto authResultDto = new AuthResultDto(
                new UserDto(id, "Joel", "Tariku", "wavyjoel", "joel@example.com"),
                "jwt-token"
        );

        when(authMapper.toSignUpRequest(validDto)).thenReturn(signupRequest);
        when(authService.signup(signupRequest)).thenReturn(authResult);
        when(authMapper.toAuthResultDTO(authResult)).thenReturn(authResultDto);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.id").value(id.toString()))
                .andExpect(jsonPath("$.user.username").value("wavyjoel"))
                .andExpect(jsonPath("$.user.email").value("joel@example.com"));
    }

    @Test
    void signup_duplicateEmail_returns409() throws Exception {
        when(authMapper.toSignUpRequest(validDto)).thenReturn(signupRequest);
        when(authService.signup(signupRequest))
                .thenThrow(new EmailAlreadyExistsException("joel@example.com"));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("The email 'joel@example.com' is already registered."));
    }

    @Test
    void signup_duplicateUsername_returns409() throws Exception {
        when(authMapper.toSignUpRequest(validDto)).thenReturn(signupRequest);
        when(authService.signup(signupRequest))
                .thenThrow(new UsernameAlreadyExistsException("wavyjoel"));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Their is already a user with the username 'wavyjoel'."));
    }

    @Test
    void signup_blankUsername_returns400WithValidationDetails() throws Exception {
        SignUpRequestDto invalidDto = new SignUpRequestDto("Joel", "Tariku", "", "joel@example.com", "secret123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void signup_invalidEmail_returns400() throws Exception {
        SignUpRequestDto invalidDto = new SignUpRequestDto("Joel", "Tariku", "wavyjoel", "not-an-email", "secret123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_passwordTooShort_returns400() throws Exception {
        SignUpRequestDto invalidDto = new SignUpRequestDto("Joel", "Tariku", "wavyjoel", "joel@example.com", "short");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
