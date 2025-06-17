package com.shashi.userservice.services;

import com.shashi.userservice.entities.User;
import com.shashi.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public User getProfile(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(Long id, User updated) {
        User user = getProfile(id);
        user.setUsername(updated.getUsername());
        user.setEmail(updated.getEmail());
        user.setPassword(updated.getPassword());
        return userRepository.save(user);
    }
}
