package com.example.stockwatchlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    // You would inject StockService or ReportService here if needed

    @GetMapping("/reports")
    public String viewPerformanceReport(Model model) {
        // 1. (Future step): Fetch data for the report (e.g., user's portfolio performance)
        // model.addAttribute("performanceData", reportService.generateReport(userId));

        // 2. Return the name of the Thymeleaf template (reports.html)
        return "reports";
    }
}