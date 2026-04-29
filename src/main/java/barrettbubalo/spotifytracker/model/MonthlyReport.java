package barrettbubalo.spotifytracker.model;

import java.time.YearMonth;

/**
 * Monthly Report for a month's listening statistics
 */
public class MonthlyReport extends Report {
    private YearMonth month;

    public MonthlyReport(Account account, YearMonth month) {
        super(account);
        this.month = month;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    @Override
    public String getPeriodType() {
        return "MONTHLY";
    }

    @Override
    public String getPeriodDescription() {
        return month.toString();
    }
}

