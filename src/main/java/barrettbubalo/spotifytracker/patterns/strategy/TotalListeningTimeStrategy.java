package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.ListeningRecord;
import barrettbubalo.spotifytracker.model.MusicEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalListeningTimeStrategy extends RankingStrategy {

    @Override
    public List<RankedItem> rank(List<ListeningRecord> records, MusicEntityExtractor extractor) {
        Map<MusicEntity, Integer> totalDurations = new HashMap<>();

        for (ListeningRecord record : records) {
            MusicEntity item = extractor.extract(record);
            totalDurations.merge(item, record.getTrack().getDurationMs(), Integer::sum);
        }

        return buildRankedItems(totalDurations, MetricType.LISTENING_TIME);
    }
}