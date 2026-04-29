package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.MusicEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RankingStrategy implements IRankingStrategy {
   
    protected List<RankedItem> buildRankedItems(Map<MusicEntity, Integer> scores, MetricType metricType) {
        List<Map.Entry<MusicEntity, Integer>> sorted = new ArrayList<>(scores.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<RankedItem> rankedItems = new ArrayList<>();
        int rank = 1;
        int previousScore = -1;
        int previousRank = 1;

        for (Map.Entry<MusicEntity, Integer> entry : sorted) {
            MusicEntity entity = entry.getKey();
            int score = entry.getValue();

            if (score == previousScore) {
                rank = previousRank;
            } else {
                previousRank = rank;
            }

            RankedItem item = new RankedItem(entity, rank, score, metricType);

            rankedItems.add(item);
            previousScore = score;
            rank = rankedItems.size() + 1;
        }

        return rankedItems;
    }
}