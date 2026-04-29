package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.ListeningRecord;

import java.util.List;

public interface IRankingStrategy {
    List<RankedItem> rank(List<ListeningRecord> records, MusicEntityExtractor exctractor);
}