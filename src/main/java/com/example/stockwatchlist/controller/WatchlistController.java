package com.example.stockwatchlist.controller;

import com.example.stockwatchlist.model.Watchlist;
import com.example.stockwatchlist.model.User;
import com.example.stockwatchlist.repository.UserRepository;
import com.example.stockwatchlist.service.WatchlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// SECURITY IMPORTS: These are the lines the compiler cannot find.
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserRepository userRepository;

    public WatchlistController(WatchlistService watchlistService, UserRepository userRepository) {
        this.watchlistService = watchlistService;
        this.userRepository = userRepository;
    }

    // Helper method to get the logged-in User's ID
    private Long getCurrentUserId(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;
        // In UserService, we set the username as the email
        return userRepository.findByEmail(userDetails.getUsername())
                .map(User::getUserId)
                .orElse(null);
    }

    // ===================================
    // 1. Thymeleaf (MVC) Endpoints for Dashboard
    // ===================================

    // GET /watchlist: Shows the main dashboard
    @GetMapping("/watchlist")
    public String viewWatchlist(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        if (userId == null) {
            return "redirect:/login";
        }

        List<Watchlist> watchlist = watchlistService.getUserWatchlist(userId);
        model.addAttribute("watchlist", watchlist);
        return "watchlist";
    }

    // POST /watchlist/add: Adds a stock via the web form
    @PostMapping("/watchlist/add")
    public String addStockToWatchlist(
            @RequestParam Long stockId,
            @RequestParam BigDecimal targetPrice,
            @RequestParam String notes,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Long userId = getCurrentUserId(userDetails);
        if (userId == null) return "redirect:/login";

        try {
            watchlistService.addStockToWatchlist(userId, stockId, targetPrice, notes);
            redirectAttributes.addFlashAttribute("successMessage", "Stock successfully added to watchlist!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/watchlist";
    }

    // POST /watchlist/{id}/delete: Removes a stock from the watchlist
    @PostMapping("/watchlist/{id}/delete")
    public String removeWatchlistItem(@PathVariable("id") Long watchlistId, RedirectAttributes redirectAttributes) {
        try {
            watchlistService.removeWatchlistItem(watchlistId);
            redirectAttributes.addFlashAttribute("successMessage", "Watchlist item removed successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/watchlist";
    }

    // ===================================
    // 2. REST API Endpoints
    // ===================================

    // POST /api/watchlist: Add stock to user's watchlist
    @PostMapping("/api/watchlist")
    public ResponseEntity<?> addWatchlistItem(@RequestParam Long userId, @RequestParam Long stockId,
                                              @RequestParam BigDecimal targetPrice, @RequestParam String notes) {
        try {
            Watchlist item = watchlistService.addStockToWatchlist(userId, stockId, targetPrice, notes);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /api/watchlist/user/{id}: View user's watchlist
    @GetMapping("/api/watchlist/user/{id}")
    public ResponseEntity<List<Watchlist>> getUserWatchlist(@PathVariable Long id) {
        List<Watchlist> watchlist = watchlistService.getUserWatchlist(id);
        return ResponseEntity.ok(watchlist);
    }

    // PUT /api/watchlist/{id}: Update target price or notes
    @PutMapping("/api/watchlist/{id}")
    public ResponseEntity<?> updateWatchlistItem(@PathVariable Long id, @RequestParam BigDecimal targetPrice,
                                                 @RequestParam String notes) {
        try {
            Watchlist updatedItem = watchlistService.updateWatchlistItem(id, targetPrice, notes);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE /api/watchlist/{id}: Remove stock from watchlist
    @DeleteMapping("/api/watchlist/{id}")
    public ResponseEntity<Void> deleteWatchlistItem(@PathVariable Long id) {
        try {
            watchlistService.removeWatchlistItem(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
