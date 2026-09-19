package com.joeltariku.financetracker.auth;

public record SignupRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password
) {
}
