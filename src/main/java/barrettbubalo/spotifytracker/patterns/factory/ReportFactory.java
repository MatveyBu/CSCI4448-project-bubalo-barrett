package barrettbubalo.spotifytracker.patterns.factory;

import barrettbubalo.spotifytracker.model.*;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Factory Pattern implementation for creating different types of reports
 * This factory creates Report objects based on the specified period type
 */
public class ReportFactory {

    /**
     * Create a report based on the period type
     */
    public static Report createReport(Account account, String periodType) {
        return createReport(account, periodType, LocalDate.now());
    }

    /**
     * Create a report with a specific reference date
     */
    public static Report createReport(Account account, String periodType, LocalDate referenceDate) {
        return switch (periodType.toUpperCase()) {
            case "DAILY" -> new DailyReport(account, referenceDate);
            case "WEEKLY" -> createWeeklyReport(account, referenceDate);
            case "MONTHLY" -> createMonthlyReport(account, referenceDate);
            case "ANNUAL" -> createAnnualReport(account, referenceDate);
            default -> throw new IllegalArgumentException("Unknown report period type: " + periodType);
        };
    }

    /**
     * Create a daily report for a specific date
     */
    public static Report createDailyReport(Account account, LocalDate date) {
        return new DailyReport(account, date);
    }

    /**
     * Create a weekly report for the week containing the reference date
     */
    public static Report createWeeklyReport(Account account, LocalDate referenceDate) {
        // Calculate the start of the week (Monday)
        LocalDate startDate = referenceDate.minusDays(referenceDate.getDayOfWeek().getValue() - 1);
        // Calculate the end of the week (Sunday)
        LocalDate endDate = startDate.plusDays(6);
        return new WeeklyReport(account, startDate, endDate);
    }

    /**
     * Create a monthly report for the month containing the reference date
     */
    public static Report createMonthlyReport(Account account, LocalDate referenceDate) {
        YearMonth month = YearMonth.from(referenceDate);
        return new MonthlyReport(account, month);
    }

    /**
     * Create an annual report for the year containing the reference date
     */
    public static Report createAnnualReport(Account account, LocalDate referenceDate) {
        int year = referenceDate.getYear();
        return new AnnualReport(account, year);
    }

    /**
     * Create a report with explicit parameters (for weekly reports with custom dates)
     */
    public static Report createCustomWeeklyReport(Account account, LocalDate startDate, LocalDate endDate) {
        return new WeeklyReport(account, startDate, endDate);
    }

    /**
     * Create a report with explicit year and month (for monthly reports)
     */
    public static Report createCustomMonthlyReport(Account account, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return new MonthlyReport(account, yearMonth);
    }
}

