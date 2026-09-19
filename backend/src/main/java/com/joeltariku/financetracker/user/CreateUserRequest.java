package com.joeltariku.financetracker.user;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String passwordHash
) {
}
