package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.ListeningRecord;
import barrettbubalo.spotifytracker.model.MusicEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayCountStrategy extends RankingStrategy {

    @Override
    public List<RankedItem> rank(List<ListeningRecord> records, MusicEntityExtractor extractor) {
        Map<MusicEntity, Integer> playCounts = new HashMap<>();

        for (ListeningRecord record : records) {
            MusicEntity item = extractor.extract(record);
            playCounts.merge(item, 1, Integer::sum);
        }

        return buildRankedItems(playCounts, MetricType.PLAY_COUNT);
    }
}