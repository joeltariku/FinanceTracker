package com.joeltariku.financetracker.auth;

import com.joeltariku.financetracker.user.User;

public record AuthResult(
        User user,
        String token
) {
}
