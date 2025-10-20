package com.example.stockwatchlist.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId; // Matches stock_id

    @Column(nullable = false, unique = true, length = 10)
    private String symbol; // Ticker symbol (e.g., AAPL, GOOGL)

    @Column(nullable = false, length = 100)
    private String name; // Company name

    // DECIMAL(10, 2) in MySQL
    @Column(precision = 10, scale = 2)
    private BigDecimal currentPrice;

    private String market; // e.g., NASDAQ, NYSE

    // --- Relationships ---
    // A Stock can be on many watchlists (One-to-Many relationship with Watchlist)
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Watchlist> watchlists;
}