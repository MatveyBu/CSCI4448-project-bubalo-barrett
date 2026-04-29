package barrettbubalo.spotifytracker.service;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.repository.TrackRepository;
import barrettbubalo.spotifytracker.model.Track;
import barrettbubalo.spotifytracker.repository.ListeningRecordRepository;
import barrettbubalo.spotifytracker.model.ListeningRecord;
import barrettbubalo.spotifytracker.model.Artist;
import barrettbubalo.spotifytracker.repository.ArtistRepository;
import barrettbubalo.spotifytracker.model.Album;
import barrettbubalo.spotifytracker.repository.AlbumRepository;
import barrettbubalo.spotifytracker.model.ArtistBuilder;
import barrettbubalo.spotifytracker.model.AlbumBuilder;
import barrettbubalo.spotifytracker.model.TrackBuilder;
import barrettbubalo.spotifytracker.model.*;
import barrettbubalo.spotifytracker.repository.*;
import barrettbubalo.spotifytracker.patterns.observer.*;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class SpotifySyncService implements SyncEventPublisher{

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;
    
    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired 
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private static final int MAX_RECENT_TRACKS = 10;
    private static final int IGNORE_AFTER = 0;

    private List<SyncEventListener> syncListeners;

    @Autowired
    public SpotifySyncService(List<SyncEventListener> listeners) {
        this.syncListeners = listeners;
    }

    public void syncRecentlyPlayed(Account account) {
        JsonNode response = spotifyApiClient.getRecentlyPlayed(account, MAX_RECENT_TRACKS, IGNORE_AFTER);
        JsonNode items = response.get("items");

        List<ListeningRecord> newListeningRecords = new ArrayList<>();

        for (JsonNode item : items) {
            JsonNode trackNode = item.get("track");

            String trackSpotifyId = trackNode.get("id").asText();
            String trackName = trackNode.get("name").asText();
            int trackDurationMs = trackNode.get("duration_ms").asInt();

            // Main Artist Info
            String mainArtistSpotifyId = trackNode.get("artists").get(0).get("id").asText();
            String mainArtistName = trackNode.get("artists").get(0).get("name").asText();

            // Album Info
            String albumSpotifyId = trackNode.get("album").get("id").asText();
            String albumName = trackNode.get("album").get("name").asText();
            String albumImageUrl = trackNode.get("album").get("images").get(0).get("url").asText();

            // Album Artist Info
            String albumArtistSpotifyId = trackNode.get("album").get("artists").get(0).get("id").asText();
            String albumArtistName = trackNode.get("album").get("artists").get(0).get("name").asText();


            String playedAtStr = item.get("played_at").asText();
            LocalDateTime playedAt = LocalDateTime.parse(
                playedAtStr, 
                DateTimeFormatter.ISO_DATE_TIME
            );

            Artist mainArtist = artistRepository.findBySpotifyId(mainArtistSpotifyId)
                .orElseGet(() -> artistRepository.save(
                    new ArtistBuilder()
                        .withSpotifyId(mainArtistSpotifyId)
                        .withName(mainArtistName)
                        .build()
                ));

            Artist albumArtist = artistRepository.findBySpotifyId(albumArtistSpotifyId)
                .orElseGet(() -> artistRepository.save(
                    new ArtistBuilder()
                        .withSpotifyId(albumArtistSpotifyId)
                        .withName(albumArtistName)
                        .build()
                ));

            Album album = albumRepository.findBySpotifyId(albumSpotifyId)
                .orElseGet(() -> albumRepository.save(
                    new AlbumBuilder()
                        .withSpotifyId(albumSpotifyId)
                        .withName(albumName)
                        .withMainArtist(albumArtist)
                        .withImageUrl(albumImageUrl)
                        .build()
                ));

            Track track = trackRepository.findBySpotifyId(trackSpotifyId)
                .orElseGet(() -> trackRepository.save(
                    new TrackBuilder()
                        .withSpotifyId(trackSpotifyId)
                        .withName(trackName)
                        .withDurationMs(trackDurationMs)
                        .withMainArtist(mainArtist)
                        .withAlbum(album)
                        .build()
                ));     

            if (!listeningRecordRepository.existsByAccountAndTrackAndPlayedAt(account, track, playedAt)) {
                ListeningRecord record = new ListeningRecord(account, track, playedAt);
                listeningRecordRepository.save(record);
                newListeningRecords.add(record);
            }
        }

        // notify observers
        SyncEvent event = new SyncEvent(account, newListeningRecords, LocalDateTime.now());
        notifyListeners(event);
    }


    @Scheduled(fixedRate = 30000) // 30 seconds polling in ms
    public void syncAllUsers() {

        // find all accounts with a valid spotify that has verified tokens
        List<Account> accounts = accountRepository.findByRefreshTokenIsNotNull();
        for (Account account : accounts) {
            syncRecentlyPlayed(account);
        }
    }


    // --- Observer Pattern stuff ---
    public void addListener(SyncEventListener listener) {
        syncListeners.add(listener);
    }

    public void removeListener(SyncEventListener listener) {
        syncListeners.remove(listener);
    }

    public void notifyListeners(SyncEvent event) {
        for (SyncEventListener listener : syncListeners) {
            listener.onSyncComplete(event);
        }
    }
}