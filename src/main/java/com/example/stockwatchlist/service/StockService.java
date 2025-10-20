package com.example.stockwatchlist.service;

import com.example.stockwatchlist.model.Stock;
import com.example.stockwatchlist.repository.StockRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // Business Logic Method: Add a new stock
    public Stock addStock(Stock stock) throws IllegalStateException {
        // Ensure the symbol is unique before saving
        if (stockRepository.findBySymbol(stock.getSymbol()).isPresent()) {
            throw new IllegalStateException("Stock symbol " + stock.getSymbol() + " already exists.");
        }
        return stockRepository.save(stock);
    }

    // Business Logic Method: View all stocks
    public List<Stock> findAllStocks() {
        return stockRepository.findAll();
    }

    // Helper Method: Find stock by ID
    public Optional<Stock> findStockById(Long stockId) {
        return stockRepository.findById(stockId);
    }

    // Helper Method: Find stock by symbol (useful for watchlist addition)
    public Optional<Stock> findStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    // Mock Method: Simulate updating the current price
    public Stock updatePrice(Long stockId, BigDecimal newPrice) throws IllegalArgumentException {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + stockId));

        stock.setCurrentPrice(newPrice);
        return stockRepository.save(stock);
    }
}