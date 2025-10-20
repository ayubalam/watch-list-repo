package com.example.stockwatchlist.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // *** NEW IMPORT ***
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails { // *** IMPLEMENT USERDETAILS ***

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    private String contactNo;

    @Column(updatable = false)
    private LocalDateTime joinDate = LocalDateTime.now();

    // User can have multiple items in their watchlist (One-to-Many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Watchlist> watchlists;

    // ===========================================
    // IMPLEMENTATION OF UserDetails INTERFACE
    // ===========================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Simple implementation: grant a default "USER" role
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getUsername() {
        // CRUCIAL: Return the email, as this is the field used for login
        return this.email;
    }

    @Override
    public String getPassword() {
        // Return the stored password hash
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming accounts never expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming accounts are never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Assuming user is always enabled
    }
}