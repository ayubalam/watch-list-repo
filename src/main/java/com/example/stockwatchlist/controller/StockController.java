package com.example.stockwatchlist.controller;

import com.example.stockwatchlist.model.Stock;
import com.example.stockwatchlist.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Use @RestController for pure API endpoints
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // POST /api/stocks: Add new stock (Requires authentication as per SecurityConfig default)
    @PostMapping
    public ResponseEntity<?> addNewStock(@RequestBody Stock stock) {
        try {
            Stock savedStock = stockService.addStock(stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // GET /api/stocks: View all stocks (Requires authentication)
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.findAllStocks());
    }

    // PUT /api/stocks/price/{id}: Mock price update
    @PutMapping("/price/{id}")
    public ResponseEntity<?> updateStockPrice(@PathVariable Long id, @RequestParam("newPrice") String newPriceString) {
        try {
            // Validate and parse the price string
            java.math.BigDecimal newPrice = new java.math.BigDecimal(newPriceString);
            Stock updatedStock = stockService.updatePrice(id, newPrice);
            return ResponseEntity.ok(updatedStock);
        } catch (IllegalArgumentException e) {
            // Catches errors from StockService (stock not found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Catches NumberFormatException and any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price format or unexpected error: " + e.getMessage());
        }
    }
}
