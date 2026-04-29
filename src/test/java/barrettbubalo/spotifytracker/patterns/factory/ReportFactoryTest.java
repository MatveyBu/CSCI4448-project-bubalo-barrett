package barrettbubalo.spotifytracker.patterns.factory;

import barrettbubalo.spotifytracker.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReportFactoryTest {

    @Test
    void testCreateReportTypes() {
        Account account = new Account();
        LocalDate date = LocalDate.of(2023, 10, 11); // A Wednesday

        Report daily = ReportFactory.createReport(account, "DAILY", date);
        assertThat(daily).isInstanceOf(DailyReport.class);

        // Weekly: Oct 11 2023 is Wednesday. Monday was Oct 9.
        WeeklyReport weekly = (WeeklyReport) ReportFactory.createReport(account, "WEEKLY", date);
        assertThat(weekly.getStartDate()).isEqualTo(LocalDate.of(2023, 10, 9));
        assertThat(weekly.getEndDate()).isEqualTo(LocalDate.of(2023, 10, 15));

        Report monthly = ReportFactory.createReport(account, "MONTHLY", date);
        assertThat(monthly).isInstanceOf(MonthlyReport.class);

        Report annual = ReportFactory.createReport(account, "ANNUAL", date);
        assertThat(annual).isInstanceOf(AnnualReport.class);
    }

    @Test
    void testCustomReportsAndShorthands() {
        Account account = new Account();

        assertThat(ReportFactory.createReport(account, "DAILY")).isNotNull();
        assertThat(ReportFactory.createDailyReport(account, LocalDate.now())).isNotNull();
        assertThat(ReportFactory.createCustomWeeklyReport(account, LocalDate.now(), LocalDate.now())).isNotNull();

        MonthlyReport customMonth = (MonthlyReport) ReportFactory.createCustomMonthlyReport(account, 2024, 2);
        assertThat(customMonth.getMonth().getYear()).isEqualTo(2024);
        assertThat(customMonth.getMonth().getMonthValue()).isEqualTo(2);

        AnnualReport customAnnual = (AnnualReport) ReportFactory.createAnnualReport(account, LocalDate.of(2025, 1, 1));
        assertThat(customAnnual.getYear()).isEqualTo(2025);
    }

    @Test
    void testInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            ReportFactory.createReport(new Account(), "GALAXY_REPORT");
        });
    }
}