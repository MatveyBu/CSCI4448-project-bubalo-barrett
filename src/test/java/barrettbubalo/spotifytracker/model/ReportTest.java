package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class ReportTest {

    @Test
    void testDailyReport() {
        LocalDate date = LocalDate.of(2023, 10, 1);
        DailyReport report = new DailyReport(null, date);
        assertThat(report.getPeriodType()).isEqualTo("DAILY");
        assertThat(report.getPeriodDescription()).isEqualTo("2023-10-01");

        report.setDate(LocalDate.of(2024, 1, 1));
        assertThat(report.getDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void testWeeklyReport() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);
        WeeklyReport report = new WeeklyReport(null, start, end);
        assertThat(report.getPeriodType()).isEqualTo("WEEKLY");
        assertThat(report.getPeriodDescription()).contains(start.toString());

        report.setStartDate(start);
        report.setEndDate(end);
        assertThat(report.getStartDate()).isEqualTo(start);
        assertThat(report.getEndDate()).isEqualTo(end);
    }

    @Test
    void testMonthlyReport() {
        YearMonth ym = YearMonth.of(2023, 5);
        MonthlyReport report = new MonthlyReport(null, ym);
        assertThat(report.getPeriodType()).isEqualTo("MONTHLY");
        assertThat(report.getMonth()).isEqualTo(ym);

        report.setMonth(YearMonth.of(2024, 1));
        assertThat(report.getMonth()).isEqualTo(YearMonth.of(2024, 1));
    }

    @Test
    void testAnnualReport() {
        AnnualReport report = new AnnualReport(null, 2023);
        assertThat(report.getPeriodType()).isEqualTo("ANNUAL");
        assertThat(report.getYear()).isEqualTo(2023);

        report.setYear(2025);
        assertThat(report.getYear()).isEqualTo(2025);
    }

    @Test
    void testAbstractReportMethods() {
        DailyReport report = new DailyReport(null, LocalDate.now());
        report.setTopTracks(new ArrayList<>());
        report.setTopArtists(new ArrayList<>());
        report.setTopAlbums(new ArrayList<>());
        report.setTotalPlaysInPeriod(10);
        report.setTotalListeningTimeMs(5000L);

        assertThat(report.getTopTracks()).isEmpty();
        assertThat(report.getTopArtists()).isEmpty();
        assertThat(report.getTopAlbums()).isEmpty();
        assertThat(report.getTotalPlaysInPeriod()).isEqualTo(10);
        assertThat(report.getTotalListeningTimeMs()).isEqualTo(5000L);
        assertThat(report.getGeneratedAt()).isNotNull();
        assertThat(report.getAccount()).isNull();
    }
}