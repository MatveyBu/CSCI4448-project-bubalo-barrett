package barrettbubalo.spotifytracker.service;

import barrettbubalo.spotifytracker.model.*;
import barrettbubalo.spotifytracker.repository.*;
import barrettbubalo.spotifytracker.patterns.strategy.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatsService {

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;

    public List<RankedItem> getTopItems(Long accountId, MusicEntityType entityType, MetricType metricType) {
        List<ListeningRecord> records = listeningRecordRepository.findByAccountId(accountId);

        MusicEntityExtractor extractor = switch (entityType) {
            case TRACK -> MusicEntityExtractors.BY_TRACK;
            case ARTIST -> MusicEntityExtractors.BY_ARTIST;
            case ALBUM -> MusicEntityExtractors.BY_ALBUM;
        };

        RankingStrategy strategy = switch (metricType) {
            case PLAY_COUNT -> new PlayCountStrategy();
            case LISTENING_TIME -> new TotalListeningTimeStrategy();
        };

        return strategy.rank(records, extractor);
    }
}