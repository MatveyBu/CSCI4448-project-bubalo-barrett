package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.MusicEntity;
import barrettbubalo.spotifytracker.model.ListeningRecord;

public interface MusicEntityExtractor {
    MusicEntity extract(ListeningRecord record);
}