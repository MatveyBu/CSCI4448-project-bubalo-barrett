package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.*;

public class RankedItem {
    private MusicEntity musicEntity;
    private int rank;
    private int metricScore;
    private MetricType metricType;

    public RankedItem(MusicEntity musicEntity, int rank, int metricScore, MetricType metricType) {
        this.musicEntity = musicEntity;
        this.rank = rank;
        this.metricScore = metricScore;
        this.metricType = metricType;
    }

    public MusicEntityType getMusicEntityType() {
        return this.musicEntity.getType();
    }

    public String getName() {
        return this.musicEntity.getName();
    }

    public String getSpotifyId() {
        return this.musicEntity.getSpotifyId();
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
        return this.musicEntity.getImageUrl();
    }
}