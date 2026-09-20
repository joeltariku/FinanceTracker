package com.joeltariku.financetracker.auth.dto;

import com.joeltariku.financetracker.user.dto.UserDto;

public record AuthResultDto(
        UserDto user,
        String token
) {
}
