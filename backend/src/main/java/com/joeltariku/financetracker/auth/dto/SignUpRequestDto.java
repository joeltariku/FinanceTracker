package com.joeltariku.financetracker.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @NotBlank @Size(min = 1, max = 50, message = ERROR_MESSAGE_FIRST_NAME_LENGTH)
        String firstName,
        @NotBlank @Size(min = 1, max = 50, message = ERROR_MESSAGE_LAST_NAME_LENGTH)
        String lastName,
        @NotBlank @Size(min = 1, max = 25, message = ERROR_MESSAGE_USERNAME_LENGTH)
        String username,
        @NotNull @Email
        String email,
        @NotBlank @Size(min = 8, max = 64, message = ERROR_MESSAGE_PASSWORD_LENGTH)
        String password
){
    private static final String ERROR_MESSAGE_FIRST_NAME_LENGTH =
            "First name must be between 1 and 50 characters.";

    private static final String ERROR_MESSAGE_LAST_NAME_LENGTH =
            "Last name must be between 1 and 50 characters.";

    private static final String ERROR_MESSAGE_USERNAME_LENGTH =
            "Username must be between 1 and 25 characters.";

    private static final String ERROR_MESSAGE_PASSWORD_LENGTH =
            "Password must be between 8 and 64 characters.";
}
