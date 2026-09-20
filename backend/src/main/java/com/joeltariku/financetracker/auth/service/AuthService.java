package com.joeltariku.financetracker.auth.service;

import com.joeltariku.financetracker.auth.AuthResult;
import com.joeltariku.financetracker.auth.SignupRequest;

public interface AuthService {
    AuthResult signup(SignupRequest request);
}
