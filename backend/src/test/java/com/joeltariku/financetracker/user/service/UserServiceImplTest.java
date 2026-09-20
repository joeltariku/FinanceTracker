package com.joeltariku.financetracker.user.service;

import com.joeltariku.financetracker.user.User;
import com.joeltariku.financetracker.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByEmail_existingUser_returnsIt() {
        User user = User.builder()
                .firstName("Joel").lastName("Tariku").username("wavyjoel")
                .email("joel@example.com").passwordHash("hash").build();
        when(userRepository.findByEmail("joel@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("joel@example.com");

        assertTrue(result.isPresent());
        assertSame(user, result.get());
    }

    @Test
    void findByEmail_unknownEmail_returnsEmpty() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        assertTrue(userService.findByEmail("nobody@example.com").isEmpty());
    }

    @Test
    void findByUsername_existingUser_returnsIt() {
        User user = User.builder()
                .firstName("Joel").lastName("Tariku").username("wavyjoel")
                .email("joel@example.com").passwordHash("hash").build();
        when(userRepository.findByUsername("wavyjoel")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("wavyjoel");

        assertTrue(result.isPresent());
        assertSame(user, result.get());
    }

    @Test
    void findByUsername_unknownUsername_returnsEmpty() {
        when(userRepository.findByUsername("gabe")).thenReturn(Optional.empty());

        assertTrue(userService.findByUsername("gabe").isEmpty());
    }
}