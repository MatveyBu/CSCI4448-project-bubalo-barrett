package barrettbubalo.spotifytracker.model;

import java.time.LocalDate;

/**
 * Weekly Report for a week's listening statistics
 */
public class WeeklyReport extends Report {
    private LocalDate startDate;
    private LocalDate endDate;

    public WeeklyReport(Account account, LocalDate startDate, LocalDate endDate) {
        super(account);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getPeriodType() {
        return "WEEKLY";
    }

    @Override
    public String getPeriodDescription() {
        return startDate + " to " + endDate;
    }
}

