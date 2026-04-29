package barrettbubalo.spotifytracker.service;

import barrettbubalo.spotifytracker.model.*;
import barrettbubalo.spotifytracker.repository.*;
import barrettbubalo.spotifytracker.patterns.factory.ReportFactory;
import barrettbubalo.spotifytracker.patterns.strategy.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating reports for different time periods
 */
@Service
public class ReportService {

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;

    @Autowired
    private StatsService statsService;

    /**
     * Generate a report for a specific period type (DAILY, WEEKLY, MONTHLY, ANNUAL)
     */
    public Report generateReport(Long accountId, String periodType) {
        return generateReport(accountId, periodType, LocalDate.now());
    }

    /**
     * Generate a report for a specific period type with a reference date
     */
    public Report generateReport(Long accountId, String periodType, LocalDate referenceDate) {
        Account account = new Account();
        account.setId(accountId);

        // Create the report using factory
        Report report = ReportFactory.createReport(account, periodType, referenceDate);

        // Get listening records for the period
        List<ListeningRecord> recordsInPeriod = getRecordsForPeriod(accountId, report);

        // Calculate statistics
        calculateReportStatistics(report, recordsInPeriod);

        return report;
    }

    /**
     * Get listening records that fall within the report period
     */
    private List<ListeningRecord> getRecordsForPeriod(Long accountId, Report report) {
        List<ListeningRecord> allRecords = listeningRecordRepository.findByAccountId(accountId);

        if (report instanceof DailyReport) {
            DailyReport dailyReport = (DailyReport) report;
            LocalDateTime startOfDay = dailyReport.getDate().atStartOfDay();
            LocalDateTime endOfDay = dailyReport.getDate().atTime(23, 59, 59);
            return filterRecordsByDateRange(allRecords, startOfDay, endOfDay);

        } else if (report instanceof WeeklyReport) {
            WeeklyReport weeklyReport = (WeeklyReport) report;
            LocalDateTime startOfWeek = weeklyReport.getStartDate().atStartOfDay();
            LocalDateTime endOfWeek = weeklyReport.getEndDate().atTime(23, 59, 59);
            return filterRecordsByDateRange(allRecords, startOfWeek, endOfWeek);

        } else if (report instanceof MonthlyReport) {
            MonthlyReport monthlyReport = (MonthlyReport) report;
            YearMonth month = monthlyReport.getMonth();
            LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
            return filterRecordsByDateRange(allRecords, startOfMonth, endOfMonth);

        } else if (report instanceof AnnualReport) {
            AnnualReport annualReport = (AnnualReport) report;
            LocalDateTime startOfYear = LocalDate.of(annualReport.getYear(), 1, 1).atStartOfDay();
            LocalDateTime endOfYear = LocalDate.of(annualReport.getYear(), 12, 31).atTime(23, 59, 59);
            return filterRecordsByDateRange(allRecords, startOfYear, endOfYear);
        }

        return new ArrayList<>();
    }

    /**
     * Filter records by date range
     */
    private List<ListeningRecord> filterRecordsByDateRange(List<ListeningRecord> records, LocalDateTime start, LocalDateTime end) {
        return records.stream()
                .filter(record -> {
                    LocalDateTime playedAt = record.getPlayedAt();
                    return !playedAt.isBefore(start) && !playedAt.isAfter(end);
                })
                .collect(Collectors.toList());
    }

    /**
     * Calculate statistics for the report
     */
    private void calculateReportStatistics(Report report, List<ListeningRecord> recordsInPeriod) {
        // Total plays in period
        report.setTotalPlaysInPeriod(recordsInPeriod.size());

        // Total listening time
        long totalListeningTimeMs = recordsInPeriod.stream()
                .mapToLong(record -> record.getTrack().getDurationMs())
                .sum();
        report.setTotalListeningTimeMs(totalListeningTimeMs);

        // Get top tracks, artists, and albums
        List<RankedItem> topTracks = rankItemsByPlayCount(recordsInPeriod, MusicEntityExtractors.BY_TRACK, 10);
        List<RankedItem> topArtists = rankItemsByPlayCount(recordsInPeriod, MusicEntityExtractors.BY_ARTIST, 10);
        List<RankedItem> topAlbums = rankItemsByPlayCount(recordsInPeriod, MusicEntityExtractors.BY_ALBUM, 10);

        // Convert to DTOs
        report.setTopTracks(convertToDTO(topTracks));
        report.setTopArtists(convertToDTO(topArtists));
        report.setTopAlbums(convertToDTO(topAlbums));
    }

    /**
     * Rank items by play count
     */
    private List<RankedItem> rankItemsByPlayCount(List<ListeningRecord> records, MusicEntityExtractor extractor, int limit) {
        PlayCountStrategy strategy = new PlayCountStrategy();
        List<RankedItem> rankedItems = strategy.rank(records, extractor);
        return rankedItems.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Convert RankedItem to RankedItemDTO
     */
    private List<RankedItemDTO> convertToDTO(List<RankedItem> rankedItems) {
        return rankedItems.stream()
                .map(item -> new RankedItemDTO(
                        item.getName(),
                        item.getSpotifyId(),
                        item.getImageUrl(),
                        item.getRank(),
                        item.getMetricScore(),
                        item.getMusicEntityType().toString()
                ))
                .collect(Collectors.toList());
    }


    /**
     * Get all available years for which listening data exists
     */
    public List<Integer> getAvailableYears(Long accountId) {
        List<ListeningRecord> allRecords = listeningRecordRepository.findByAccountId(accountId);
        return allRecords.stream()
                .map(record -> record.getPlayedAt().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get all available months for a given year
     */
    public List<Integer> getAvailableMonths(Long accountId, int year) {
        List<ListeningRecord> allRecords = listeningRecordRepository.findByAccountId(accountId);
        return allRecords.stream()
                .filter(record -> record.getPlayedAt().getYear() == year)
                .map(record -> record.getPlayedAt().getMonthValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get all available weeks for a given year and month
     */
    public List<Map<String, LocalDate>> getAvailableWeeks(Long accountId, int year, int month) {
        List<ListeningRecord> allRecords = listeningRecordRepository.findByAccountId(accountId);
        YearMonth yearMonth = YearMonth.of(year, month);

        return allRecords.stream()
                .filter(record -> YearMonth.from(record.getPlayedAt()).equals(yearMonth))
                .map(record -> {
                    LocalDate date = record.getPlayedAt().toLocalDate();
                    LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() - 1);
                    return weekStart;
                })
                .distinct()
                .sorted()
                .map(weekStart -> Map.of(
                        "start", weekStart,
                        "end", weekStart.plusDays(6)
                ))
                .collect(Collectors.toList());
    }
}


