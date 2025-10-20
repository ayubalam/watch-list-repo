package com.example.stockwatchlist.service;

import com.example.stockwatchlist.model.User;
import com.example.stockwatchlist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // We will need a PasswordEncoder Bean defined later (in a Config class)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Business Logic Method: Register a new user
    public User registerNewUser(User user) throws IllegalStateException {
        // 1. Check for unique email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email " + user.getEmail() + " already taken.");
        }

        // 2. Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setJoinDate(LocalDateTime.now()); // Ensure join date is set on creation

        return userRepository.save(user);
    }

    // Business Logic Method: Get all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Spring Security Requirement: Load User by Username (which is the email in our case)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the User entity from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // ✅ FIX: Directly return the User entity, which now implements UserDetails.
        return user;
    }
}