package com.example.stockwatchlist.repository;

import com.example.stockwatchlist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom Query Method: Spring Data JPA will automatically implement this
    // Finds a User by their unique email address. Crucial for login/security.
    Optional<User> findByEmail(String email);
}