package com.example.stockwatchlist.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "stock_id"})) // Enforces the unique constraint from SQL
@Data
@NoArgsConstructor
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long watchlistId; // Matches watchlist_id

    // --- Relationships (Many-to-One) ---

    // Many Watchlist entries belong to one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Maps to the user_id FK column
    private User user;

    // Many Watchlist entries reference one Stock
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false) // Maps to the stock_id FK column
    private Stock stock;

    // --- Watchlist Specific Attributes ---

    // DECIMAL(10, 2) in MySQL
    @Column(precision = 10, scale = 2)
    private BigDecimal targetPrice;

    @Lob // For TEXT type in SQL
    private String notes;

    @Column(updatable = false)
    private LocalDateTime addedDate = LocalDateTime.now(); // Maps to added_date TIMESTAMP
}