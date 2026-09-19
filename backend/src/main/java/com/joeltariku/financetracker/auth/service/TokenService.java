package com.joeltariku.financetracker.auth.service;

public interface TokenService {
    String generateToken(String userId);
}
