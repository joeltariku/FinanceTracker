package com.joeltariku.financetracker.user.service;

import com.joeltariku.financetracker.user.CreateUserRequest;
import com.joeltariku.financetracker.user.User;
import com.joeltariku.financetracker.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                request.passwordHash()
        );

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }
}
