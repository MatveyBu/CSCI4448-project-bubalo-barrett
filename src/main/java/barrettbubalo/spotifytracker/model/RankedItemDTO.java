package barrettbubalo.spotifytracker.model;

/**
 * DTO for ranked items to be included in reports
 * Immutable - created in service layer, serialized to JSON for API responses
 */
public class RankedItemDTO {
    private final String name;
    private final String spotifyId;
    private final String imageUrl;
    private final int rank;
    private final int metricScore;
    private final String musicEntityType;

    public RankedItemDTO(String name, String spotifyId, String imageUrl, int rank, int metricScore, String musicEntityType) {
        this.name = name;
        this.spotifyId = spotifyId;
        this.imageUrl = imageUrl;
        this.rank = rank;
        this.metricScore = metricScore;
        this.musicEntityType = musicEntityType;
    }

    // Getters only (for Jackson JSON serialization)
    public String getName() {
        return name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getRank() {
        return rank;
    }

    public int getMetricScore() {
        return metricScore;
    }

    public String getMusicEntityType() {
        return musicEntityType;
    }
}

