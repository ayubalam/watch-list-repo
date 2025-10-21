package com.example.stockwatchlist.controller;

import com.example.stockwatchlist.service.ReportService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final ReportService reportService;

    // Inject ReportService via constructor
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public String viewPerformanceReport(Model model) {
        // FIX: Using a hardcoded ID (1L) for demo purposes, as ReportService expects a Long ID.
        // In a complete application, you would perform a database lookup of the user's ID
        // using the authenticated username (email) found in the SecurityContext.
        Long userId = 1L;

        try {
            // Optional: Retrieve the username (email) for display/context
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            // 1. Call ReportService to generate the report string
            String report = reportService.generatePerformanceReport(userId);

            // 2. Add the report output to the model
            model.addAttribute("reportOutput", report);
            model.addAttribute("userEmail", userEmail);

        } catch (Exception e) {
            // Log the error in a real app
            model.addAttribute("reportOutput", "ERROR: Could not retrieve user context or generate report.");
        }

        // 3. Return the name of the Thymeleaf template
        return "reports";
    }
}