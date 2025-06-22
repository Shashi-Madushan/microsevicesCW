package com.shashi.userservice.services;

import com.shashi.userservice.entities.User;
import com.shashi.userservice.repository.UserRepository;


import com.shashi.userservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        return userRepository.save(user);
    }

    public Map<String, String>  login(String email, String password) {
        Map<String, String> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            response.put("error", "User not found");
            return response;
        }
        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            response.put("error", "Invalid credentials");
            return response;
        }


        String token = jwtUtil.generateToken(user.getUsername(),user.getRole());
        response.put("token", token);
        response.put("userRole", user.getRole().name());
        response.put("userName", user.getUsername());
        return response;
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
