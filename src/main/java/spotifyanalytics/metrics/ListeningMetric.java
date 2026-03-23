package spotifyanalytics.metrics;

import spotifyanalytics.metrics.MetricResult;
import spotifyanalytics.domain.PlayedTrack;
import java.util.List;

public interface ListeningMetric<ResultType> {
    String getName();
    MetricResult<ResultType> compute(List<PlayedTrack> history);
}

