package com.example.stockwatchlist.service;

import com.example.stockwatchlist.model.Watchlist;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {

    private final WatchlistService watchlistService;

    public ReportService(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    // Mocked Business Logic Method: Generate stock performance report
    public String generatePerformanceReport(Long userId) {
        List<Watchlist> userWatchlist = watchlistService.getUserWatchlist(userId);

        if (userWatchlist.isEmpty()) {
            return "No stocks found in user's watchlist to generate a report.";
        }

        StringBuilder report = new StringBuilder();
        report.append("--- Stock Performance Report for User ID: ").append(userId).append(" ---\n");

        for (Watchlist item : userWatchlist) {
            String symbol = item.getStock().getSymbol();
            BigDecimal currentPrice = item.getStock().getCurrentPrice();
            BigDecimal targetPrice = item.getTargetPrice();

            String status;
            if (currentPrice != null && targetPrice != null) {
                if (currentPrice.compareTo(targetPrice) >= 0) {
                    status = "Target Reached! (Alert: BUY/SELL)";
                } else {
                    status = "Below Target Price. Current: " + currentPrice;
                }
            } else {
                status = "Price data unavailable.";
            }

            report.append(String.format("Stock: %s | Target: %s | Status: %s\n", symbol, targetPrice, status));
        }

        report.append("----------------------------------------------------\n");
        report.append("NOTE: This is a mocked report. Alerts would be triggered here.");

        return report.toString();
    }
}