package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.ListeningRecord;
import barrettbubalo.spotifytracker.model.Track;
import barrettbubalo.spotifytracker.model.MusicEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayCountStrategy implements RankingStrategy {

    @Override
    public List<RankedItem> rank(List<ListeningRecord> records, MusicEntityExtractor extractor) {
        
        Map<MusicEntity, Integer> playCounts = new HashMap<>();

        for (ListeningRecord record : records) {
            MusicEntity item = extractor.extract(record);
            playCounts.merge(item, 1, Integer::sum);
        }

        List<Map.Entry<MusicEntity, Integer>> sorted = new ArrayList<>(playCounts.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<RankedItem> rankedItems = new ArrayList<>();
        int rank = 1;
        int previousCount = -1;
        int previousRank = 1;

        for (Map.Entry<MusicEntity, Integer> entry : sorted) {
            MusicEntity entity = entry.getKey();
            int playCount = entry.getValue();

            if (playCount == previousCount) {
                rank = previousRank;
            } else {
                previousRank = rank;
            }

            RankedItem item = new RankedItem(
                entity.getEntityType(),
                entity.getName(),
                entity.getSpotifyId(),
                rank,
                playCount,
                0
            );

            rankedItems.add(item);
            previousCount = playCount;
            rank = rankedItems.size() + 1;
        }

        return rankedItems;
    }
}