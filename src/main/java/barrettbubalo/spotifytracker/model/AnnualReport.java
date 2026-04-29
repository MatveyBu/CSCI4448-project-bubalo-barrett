package barrettbubalo.spotifytracker.model;

/**
 * Annual Report for a year's listening statistics
 */
public class AnnualReport extends Report {
    private int year;

    public AnnualReport(Account account, int year) {
        super(account);
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String getPeriodType() {
        return "ANNUAL";
    }

    @Override
    public String getPeriodDescription() {
        return String.valueOf(year);
    }
}

