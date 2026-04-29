package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.ListeningRecord;

import java.util.List;

public interface RankingStrategy {
    List<RankedItem> rank(List<ListeningRecord> records);
}