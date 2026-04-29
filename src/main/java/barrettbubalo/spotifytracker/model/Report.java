package barrettbubalo.spotifytracker.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Abstract Report class for different time period reports
 */
public abstract class Report {
    protected Account account;
    protected LocalDateTime generatedAt;
    protected List<RankedItemDTO> topTracks;
    protected List<RankedItemDTO> topArtists;
    protected List<RankedItemDTO> topAlbums;
    protected int totalPlaysInPeriod;
    protected long totalListeningTimeMs;

    protected Report(Account account) {
        this.account = account;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters
    public Account getAccount() {
        return account;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public List<RankedItemDTO> getTopTracks() {
        return topTracks;
    }

    public List<RankedItemDTO> getTopArtists() {
        return topArtists;
    }

    public List<RankedItemDTO> getTopAlbums() {
        return topAlbums;
    }

    public int getTotalPlaysInPeriod() {
        return totalPlaysInPeriod;
    }

    public long getTotalListeningTimeMs() {
        return totalListeningTimeMs;
    }

    // Setters
    public void setTopTracks(List<RankedItemDTO> topTracks) {
        this.topTracks = topTracks;
    }

    public void setTopArtists(List<RankedItemDTO> topArtists) {
        this.topArtists = topArtists;
    }

    public void setTopAlbums(List<RankedItemDTO> topAlbums) {
        this.topAlbums = topAlbums;
    }

    public void setTotalPlaysInPeriod(int totalPlaysInPeriod) {
        this.totalPlaysInPeriod = totalPlaysInPeriod;
    }

    public void setTotalListeningTimeMs(long totalListeningTimeMs) {
        this.totalListeningTimeMs = totalListeningTimeMs;
    }

    /**
     * Get the period type for this report
     */
    public abstract String getPeriodType();

    /**
     * Get a description of the period covered by this report
     */
    public abstract String getPeriodDescription();
}

