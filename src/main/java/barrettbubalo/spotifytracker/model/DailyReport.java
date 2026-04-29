package barrettbubalo.spotifytracker.model;

import java.time.LocalDate;

/**
 * Daily Report for a single day's listening statistics
 */
public class DailyReport extends Report {
    private LocalDate date;

    public DailyReport(Account account, LocalDate date) {
        super(account);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String getPeriodType() {
        return "DAILY";
    }

    @Override
    public String getPeriodDescription() {
        return date.toString();
    }
}

