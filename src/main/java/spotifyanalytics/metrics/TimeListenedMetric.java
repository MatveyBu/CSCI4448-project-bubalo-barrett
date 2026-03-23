package spotifyanalytics.metrics;

import spotifyanalytics.domain.PlayedTrack;

import java.util.List;

public class TimeListenedMetric implements ListeningMetric<Long> {

    @Override
    public String getName() {return "Total Time Listened (ms)";}

    @Override
    public MetricResult<Long> compute(List<PlayedTrack> history){
        long totalMs = history.stream().mapToLong(playedTrack -> playedTrack.getTrack().getDurationMs()).sum();

        return new MetricResult<>(getName(), totalMs);
    }
}
