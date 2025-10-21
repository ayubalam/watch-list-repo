package com.example.stockwatchlist.controller;

import com.example.stockwatchlist.model.User;
import com.example.stockwatchlist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ===================================
    // 1. Thymeleaf (MVC) Endpoints for Registration
    // ===================================

    // GET /register: Show the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // POST /register: Process the registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerNewUser(user);
            // Redirect to login page on success
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            // Redirect back to registration with error
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // GET /login: Shows the login page (configured in SecurityConfig, but good to have a simple mapping)
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ===================================
    // 2. REST API Endpoints
    // ===================================

    // POST /api/users: Add new user (This is primarily for REST API testing, though /register handles web users)
    @PostMapping("/api/users")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerNewUser(user);
            // Don't send the password back
            savedUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // GET /api/users: View all users (Requires authentication, due to SecurityConfig)
    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        // Note: For production, you should filter/mask sensitive data like contact_no or implement proper DTOs.
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/login-simple")
    public String loginSimple() {
        return "login-simple";
    }
}