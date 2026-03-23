package spotifyanalytics.metrics;

import spotifyanalytics.domain.PlayedTrack;

import java.util.*;
import java.util.stream.Collectors;

public class TopTracksMetric implements spotifyanalytics.metrics.ListeningMetric<List<String>> {
    private final int limit;

    public TopTracksMetric(int limit){
        this.limit = limit;
    }

    @Override
    public String getName(){return "Top Tracks";}

    @Override
    public MetricResult<List<String>> compute(List<PlayedTrack> history){
        Map<String, Long> counts = history.stream()
                .collect(Collectors.groupingBy(playedTrack -> playedTrack.getTrack().getTitle(), Collectors.counting()));

        List<String> topTracks= counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return new MetricResult<>(getName(), topTracks);
    }
}
