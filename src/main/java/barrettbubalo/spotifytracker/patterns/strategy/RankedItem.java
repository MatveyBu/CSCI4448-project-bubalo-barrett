package barrettbubalo.spotifytracker.patterns.strategy;

public class RankedItem {
    private RankedItemType type;
    private String name;
    private String spotifyId;
    private int rank;
    private int metricScore;
    private MetricType metricType;
    private String imageUrl;

    public RankedItem(RankedItemType type, String name, String spotifyId, int rank, int metricScore, MetricType metricType) {
        this.type = type;
        this.name = name;
        this.spotifyId = spotifyId;
        this.rank = rank;
        this.metricScore = metricScore;
        this.metricType = metricType;
    }

    public RankedItemType getType() {
        return this.type;
    }

    public void setType(RankedItemType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMetricScore() {
        return this.metricScore;
    }

    public void setMetricScore(int metricScore) {
        this.metricScore = metricScore;
    }

    public MetricType getMetricType() {
        return this.metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}