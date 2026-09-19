package com.joeltariku.financetracker.user.service;

import com.joeltariku.financetracker.user.CreateUserRequest;
import com.joeltariku.financetracker.user.User;

import java.util.Optional;

public interface UserService {
    User createUser(CreateUserRequest request);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
