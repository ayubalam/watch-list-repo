package com.example.stockwatchlist.controller;

import com.example.stockwatchlist.model.Stock;
import com.example.stockwatchlist.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal; // Ensure this import is present
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // POST /api/stocks: Add new stock
    @PostMapping
    public ResponseEntity<?> addNewStock(@RequestBody Stock stock) {
        try {
            Stock savedStock = stockService.addStock(stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // GET /api/stocks: View all stocks
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.findAllStocks());
    }

    // PUT /api/stocks/price/{id}: Mock price update
    @PutMapping("/price/{id}")
    public ResponseEntity<?> updateStockPrice(@PathVariable Long id, @RequestParam("newPrice") String newPriceString) {
        try {
            // Validate and parse the price string
            BigDecimal newPrice = new BigDecimal(newPriceString);
            Stock updatedStock = stockService.updatePrice(id, newPrice);
            return ResponseEntity.ok(updatedStock);
        } catch (NumberFormatException e) {
            // Catches error if newPriceString is not a valid number
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid price format: Price must be a valid number.");
        } catch (IllegalArgumentException e) {
            // Catches errors from StockService (e.g., stock not found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // General fallback for any other unexpected error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}