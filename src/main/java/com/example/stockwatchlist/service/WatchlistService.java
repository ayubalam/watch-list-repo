package com.example.stockwatchlist.service;

import com.example.stockwatchlist.model.Stock;
import com.example.stockwatchlist.model.User;
import com.example.stockwatchlist.model.Watchlist;
import com.example.stockwatchlist.repository.WatchlistRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserService userService;
    private final StockService stockService;

    public WatchlistService(WatchlistRepository watchlistRepository, UserService userService, StockService stockService) {
        this.watchlistRepository = watchlistRepository;
        this.userService = userService;
        this.stockService = stockService;
    }

    // Business Logic Method: Add stock to a user's watchlist
    @Transactional
    public Watchlist addStockToWatchlist(Long userId, Long stockId, BigDecimal targetPrice, String notes) throws IllegalArgumentException, IllegalStateException {
        // 1. Check if user exists (We don't need to check user exists, as it's typically authenticated, but good practice)
        User user = userService.findAllUsers().stream() // Simplified check, ideally use a findById in UserService
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 2. Check if stock exists
        Stock stock = stockService.findStockById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        // 3. Check if the stock is ALREADY in the user's watchlist
        if (watchlistRepository.findByUserUserIdAndStockStockId(userId, stockId).isPresent()) {
            throw new IllegalStateException("Stock " + stock.getSymbol() + " is already in the user's watchlist.");
        }

        // 4. Create and save the new Watchlist entry
        Watchlist item = new Watchlist();
        item.setUser(user);
        item.setStock(stock);
        item.setTargetPrice(targetPrice);
        item.setNotes(notes);

        return watchlistRepository.save(item);
    }

    // Business Logic Method: View a user's entire watchlist
    public List<Watchlist> getUserWatchlist(Long userId) {
        return watchlistRepository.findByUserUserId(userId);
    }

    // Business Logic Method: Update target price and notes
    public Watchlist updateWatchlistItem(Long watchlistId, BigDecimal targetPrice, String notes) throws IllegalArgumentException {
        Watchlist item = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist item not found with ID: " + watchlistId));

        item.setTargetPrice(targetPrice);
        item.setNotes(notes);

        return watchlistRepository.save(item);
    }

    // Business Logic Method: Remove stock from watchlist
    public void removeWatchlistItem(Long watchlistId) throws IllegalArgumentException {
        if (!watchlistRepository.existsById(watchlistId)) {
            throw new IllegalArgumentException("Watchlist item not found with ID: " + watchlistId);
        }
        watchlistRepository.deleteById(watchlistId);
    }
}