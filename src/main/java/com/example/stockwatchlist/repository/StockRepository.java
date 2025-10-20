package com.example.stockwatchlist.repository;

import com.example.stockwatchlist.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // Custom Query Method: Finds a Stock by its unique symbol (e.g., AAPL)
    Optional<Stock> findBySymbol(String symbol);
}