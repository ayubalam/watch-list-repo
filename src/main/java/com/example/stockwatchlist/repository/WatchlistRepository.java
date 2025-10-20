package com.example.stockwatchlist.repository;

import com.example.stockwatchlist.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // <-- This import was missing and caused the error

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    // Custom Query Method: Finds all Watchlist entries for a given user ID.
    // The property must match the field name in the Watchlist entity (user)
    List<Watchlist> findByUserUserId(Long userId);

    // Custom Query Method: Finds a specific watchlist item by user and stock.
    // Useful for checking if an item already exists before adding it.
    Optional<Watchlist> findByUserUserIdAndStockStockId(Long userId, Long stockId);
}