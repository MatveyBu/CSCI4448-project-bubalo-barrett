package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.ListeningRecord;
import barrettbubalo.spotifytracker.model.MusicEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalListeningTimeStrategy implements RankingStrategy {

    @Override
    public List<RankedItem> rank(List<ListeningRecord> records, MusicEntityExtractor extractor) {

        Map<MusicEntity, Integer> totalDurations = new HashMap<>();

        for (ListeningRecord record : records) {
            MusicEntity item = extractor.extract(record);
            totalDurations.merge(item, record.getTrack().getDurationMs(), Integer::sum);
        }

        List<Map.Entry<MusicEntity, Integer>> sorted = new ArrayList<>(totalDurations.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<RankedItem> rankedItems = new ArrayList<>();
        int rank = 1;
        int previousDuration = -1;
        int previousRank = 1;

        for (Map.Entry<MusicEntity, Integer> entry : sorted) {
            MusicEntity entity = entry.getKey();
            int totalDuration = entry.getValue();

            if (totalDuration == previousDuration) {
                rank = previousRank;
            } else {
                previousRank = rank;
            }

            RankedItem item = new RankedItem(
                entity,
                rank,
                totalDuration,
                MetricType.LISTENING_TIME
            );

            rankedItems.add(item);
            previousDuration = totalDuration;
            rank = rankedItems.size() + 1;
        }

        return rankedItems;
    }
}