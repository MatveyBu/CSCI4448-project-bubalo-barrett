package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.model.Report;
import barrettbubalo.spotifytracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller for generating and retrieving reports
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Generate a report for a specific period type
     * Endpoint: GET /api/reports/generate?type=DAILY|WEEKLY|MONTHLY|ANNUAL
     */
    @GetMapping("/generate")
    public ResponseEntity<?> generateReport(@RequestParam String type) {
        try {
            Long accountId = 1L; // hardcoded for testing (TODO: restore session-based auth)
            Report report = reportService.generateReport(accountId, type);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to generate report: " + e.getMessage()));
        }
    }

    /**
     * Generate a report for a specific date
     * Endpoint: GET /api/reports/generate?type=DAILY|WEEKLY|MONTHLY|ANNUAL&date=YYYY-MM-DD
     */
    @GetMapping("/generate/date")
    public ResponseEntity<?> generateReportForDate(
            @RequestParam String type,
            @RequestParam String date) {
        try {
            Long accountId = 1L; // hardcoded for testing
            LocalDate referenceDate = LocalDate.parse(date);
            Report report = reportService.generateReport(accountId, type, referenceDate);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format. Use YYYY-MM-DD"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to generate report: " + e.getMessage()));
        }
    }

    /**
     * Generate a report for a specific month
     * Endpoint: GET /api/reports/generate/month?year=2024&month=12
     */
    @GetMapping("/generate/month")
    public ResponseEntity<?> generateMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {
        try {
            Long accountId = 1L; // hardcoded for testing
            LocalDate referenceDate = LocalDate.of(year, month, 1);
            Report report = reportService.generateReport(accountId, "MONTHLY", referenceDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid month parameters: " + e.getMessage()));
        }
    }

    /**
     * Generate a report for a specific year
     * Endpoint: GET /api/reports/generate/year?year=2024
     */
    @GetMapping("/generate/year")
    public ResponseEntity<?> generateAnnualReport(@RequestParam int year) {
        try {
            Long accountId = 1L; // hardcoded for testing
            LocalDate referenceDate = LocalDate.of(year, 1, 1);
            Report report = reportService.generateReport(accountId, "ANNUAL", referenceDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid year: " + e.getMessage()));
        }
    }

    /**
     * Get available years for reports
     * Endpoint: GET /api/reports/years
     */
    @GetMapping("/years")
    public ResponseEntity<?> getAvailableYears() {
        try {
            Long accountId = 1L; // hardcoded for testing
            List<Integer> years = reportService.getAvailableYears(accountId);
            return ResponseEntity.ok(Map.of("years", years));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get available years: " + e.getMessage()));
        }
    }

    /**
     * Get available months for a specific year
     * Endpoint: GET /api/reports/months?year=2024
     */
    @GetMapping("/months")
    public ResponseEntity<?> getAvailableMonths(@RequestParam int year) {
        try {
            Long accountId = 1L; // hardcoded for testing
            List<Integer> months = reportService.getAvailableMonths(accountId, year);
            return ResponseEntity.ok(Map.of("months", months));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get available months: " + e.getMessage()));
        }
    }

    /**
     * Get available weeks for a specific year and month
     * Endpoint: GET /api/reports/weeks?year=2024&month=12
     */
    @GetMapping("/weeks")
    public ResponseEntity<?> getAvailableWeeks(
            @RequestParam int year,
            @RequestParam int month) {
        try {
            Long accountId = 1L; // hardcoded for testing
            List<Map<String, LocalDate>> weeks = reportService.getAvailableWeeks(accountId, year, month);
            return ResponseEntity.ok(Map.of("weeks", weeks));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get available weeks: " + e.getMessage()));
        }
    }

    /**
     * Endpoint: GET /api/reports/daily - Get today's report
     */
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyReport() {
        return generateReport("DAILY");
    }

    /**
     * Endpoint: GET /api/reports/weekly - Get current week's report
     */
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyReport() {
        return generateReport("WEEKLY");
    }

    /**
     * Endpoint: GET /api/reports/monthly - Get current month's report
     */
    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyReport() {
        return generateReport("MONTHLY");
    }

    /**
     * Endpoint: GET /api/reports/annual - Get current year's report
     */
    @GetMapping("/annual")
    public ResponseEntity<?> getAnnualReport() {
        return generateReport("ANNUAL");
    }
}




