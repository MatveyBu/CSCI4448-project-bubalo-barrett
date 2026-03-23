package spotifyanalytics.metrics;

import java.util.List;

public class MetricFactory {
    private static final int DEFAULT_LIMIT = 5;
    public static List<ListeningMetric<?>> createDefaultMetrics(){
        return List.of(
                new TopArtistsMetric(DEFAULT_LIMIT),
                new TopTracksMetric(DEFAULT_LIMIT),
                new TimeListenedMetric()
        );
    }
}
