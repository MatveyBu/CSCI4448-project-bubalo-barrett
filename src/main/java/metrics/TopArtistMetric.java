package metrics;

import domain.PlayedTrack;
import java.util.*;
import java.util.stream.Collectors;


public class TopArtistMetric implements ListeningMetric<List<String>>{
    private final int limit;

    public TopArtistMetric(int limit) {
        this.limit = limit;
    }

    @Override
    public String getName(){return "Top Artists";}

    @Override
    public MetricResult<List<String>> compute (List<PlayedTrack> history){
        Map<String, Long> counts = history.stream().collect(Collectors.groupingBy(pt -> pt.getTrack().getArtist().getName(), Collectors.counting()));

    List <String> top = counts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(limit)
            .map(Map.Entry::getKey).collect(Collectors.toList());

    return new MetricResult<>(getName(), top);
    }
}
