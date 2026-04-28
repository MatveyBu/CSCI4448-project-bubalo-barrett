package barrettbubalo.spotifytracker.service;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.repository.TrackRepository;
import barrettbubalo.spotifytracker.model.Track;
import barrettbubalo.spotifytracker.repository.ListeningRecordRepository;
import barrettbubalo.spotifytracker.model.ListeningRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class SpotifySyncService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;
    
    @Autowired
    private SpotifyApiClient spotifyApiClient;

    private static final int MAX_RECENT_TRACKS = 20;
    private static final int IGNORE_AFTER = 0;

    public void syncRecentlyPlayed(Account account) {
        JsonNode response = spotifyApiClient.getRecentlyPlayed(account, MAX_RECENT_TRACKS, IGNORE_AFTER);
        JsonNode items = response.get("items");

        for (JsonNode item : items) {
            JsonNode trackNode = item.get("track");

            String spotifyId = trackNode.get("id").asText();
            String name = trackNode.get("name").asText();
            int durationMs = trackNode.get("duration_ms").asInt();
            // Add these later, they will require querying for Artist and Album objects
            // String artistName = trackNode.get("artists").get(0).get("name").asText();
            // String albumName = trackNode.get("album").get("name").asText();
            //
            String playedAtStr = item.get("played_at").asText();
            LocalDateTime playedAt = LocalDateTime.parse(
                playedAtStr, 
                DateTimeFormatter.ISO_DATE_TIME
            );

            Track track = trackRepository.findBySpotifyId(spotifyId)
                .orElseGet(() -> {
                    Track newTrack = new Track();
                    newTrack.setSpotifyId(spotifyId);
                    newTrack.setName(name);
                    newTrack.setDurationMs(durationMs);
                    return trackRepository.save(newTrack);
                });

            if (!listeningRecordRepository.existsByAccountAndTrackAndPlayedAt(account, track, playedAt)) {
                ListeningRecord record = new ListeningRecord(account, track, playedAt);
                listeningRecordRepository.save(record);
            }
        }
    }


    @Scheduled(fixedRate = 30000) // 30 seconds polling in ms
    public void syncAllUsers() {

        // find all accounts with a valid spotify that has verified tokens
        List<Account> accounts = accountRepository.findByRefreshTokenIsNotNull();
        for (Account account : accounts) {
            syncRecentlyPlayed(account);
        }
    }

}